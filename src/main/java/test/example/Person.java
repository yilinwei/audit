package test.example;

import audit.auditor.IAuditor;
import audit.mongo.IMongoBsonSerializer;
import audit.record.StringRecord;
import audit.scribe.IBuilderScribe;
import audit.scribe.ILedgerScribe;
import audit.scribe.ScribeLedger;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import java.time.LocalDateTime;

public class Person {

    private final int m_id;
    private final String m_firstName;
    private final String m_lastName;

    public static final class Builder {

        private int m_id;
        private String m_firstName;
        private String m_lastName; 

        public Builder withId(int id) {
            m_id = id;
            return this;
        }

        public Builder withFirstName(String firstName) {
            m_firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            m_lastName = lastName;
            return this;
        }

        public Person build() {
            return new Person(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getFirstName() {
        return m_firstName;
    }

    public String getLastName() {
        return m_lastName;
    }

    public int getId() {
        return m_id;
    }

    public Person(Builder builder) {
        m_id = builder.m_id;
        m_firstName = builder.m_firstName;
        m_lastName = builder.m_lastName;
    }

    public static Scribe scribe(LocalDateTime time, Person person) {
        ScribeLedger<Person> ledger = new ScribeLedger<>();
        ledger.newMilestone(time, person);
        return new Scribe(ledger, time);
    }

    public static Scribe scribe(Person person) {
        return scribe(LocalDateTime.now(), person);
    }

    static final class Serializer implements IMongoBsonSerializer<Person> {

        private BsonDocument m_document;

        @Override
        public BsonDocument setIdentifier(Person person, BsonDocument document) {
            return document.append("id", new BsonInt32(person.getId()));
        }

        @Override
        public BsonDocument getDocument() {
            return m_document;
        }

        @Override
        public void setDocument(BsonDocument document) {
            m_document = document;
        }

        @Override
        public BsonDocument serialize(Person person) {
            return setIdentifier(person, new BsonDocument())
                    .append("firstName", new BsonString(person.getFirstName()))
                    .append("lastName", new BsonString(person.getLastName()));
        }
    }

    //TODO: Turn this into an annotation processor
    static final class Scribe implements IBuilderScribe<Person, Builder>, ILedgerScribe<Person> {

        private Builder m_builder;
        private Builder m_current;
        private LocalDateTime m_time;
        private final ScribeLedger<Person> m_ledger;

        Scribe(ScribeLedger<Person> ledger, LocalDateTime time) {
            m_ledger = ledger;
            m_time = time;
            m_current = newBuilder(ledger.write(this));
        }

        public Scribe setFirstName(String firstName) {
            m_current.withFirstName(firstName);
            m_ledger.newRecord(new StringRecord("firstName", m_current.m_firstName));
            return this;
        }

        public Scribe setLastName(String lastName) {
            m_current.withLastName(lastName);
            m_ledger.newRecord(new StringRecord("lastName", m_current.m_lastName));
            return this;
        }

        public Scribe at(LocalDateTime time) {
            m_time = time;
            m_ledger.newEntry(time);
            return this;
        }

        public Scribe now() {
            m_time = LocalDateTime.now();
            return at(m_time);
        }

        public Scribe milestone() {
            m_ledger.newMilestone(m_time, this);
            return this;
        }

        @Override
        public ScribeLedger<Person> getLedger() {
            return m_ledger;
        }

        @Override
        public void setBuilder(Builder builder) {
            m_builder = builder;
        }

        @Override
        public Builder getBuilder() {
            return m_builder;
        }

        @Override
        public Builder newBuilder(Person milestone) {
            return builder()
                    .withId(milestone.getId())
                    .withFirstName(milestone.getFirstName())
                    .withLastName(milestone.getLastName());
        }

        @Override
        public Person build(Builder builder) {
            return builder.build();
        }

        @Override
        public void visit(StringRecord record) {
            switch(record.getField()) {
                case "firstName":
                    m_builder.withFirstName(record.getValue());
                    break;
                case "lastName":
                    m_builder.withLastName(record.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("unknown field for ");
            }
        }
    }

}
