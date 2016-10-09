package test.example;

import audit.auditor.IAuditor;
import audit.clerk.IClerk;
import audit.mongo.MongoBsonAuditor;

import java.time.LocalDateTime;

public final class ExampleUsage {
    public static void main(String[] args) {
        Person person = Person
            .builder()
            .withId(1)
            .withFirstName("John")
            .withLastName("Smith")
            .build();

         //record updates
         Person.Scribe scribe = Person
             .scribe(LocalDateTime.MIN, person)
             .now()
             .setFirstName("Jonny")
             .at(LocalDateTime.now().plusDays(7))
             .setFirstName("Jo")
             .milestone();

        IAuditor<Person> auditor = new MongoBsonAuditor<>(new Person.Serializer(), null);
        auditor.record(scribe, scribe.getLedger());

        IClerk<Person> clerk = null;
        clerk.next();

    }
}
