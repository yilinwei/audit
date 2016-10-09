package audit.mongo;

import audit.auditor.IAuditor;
import audit.scribe.IScribe;
import audit.scribe.ScribeLedger;
import com.mongodb.rx.client.MongoCollection;
import org.bson.BsonDateTime;
import org.bson.BsonDocument;
import rx.Observable;

import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;

public final class MongoBsonAuditor<R> implements IAuditor<R> {

    private IMongoBsonSerializer<R> m_serializer;
    private MongoCollection<BsonDocument> m_collection;

    public MongoBsonAuditor(IMongoBsonSerializer<R> serializer, MongoCollection<BsonDocument> collection) {
        m_serializer = serializer;
        m_collection = collection;
    }

    @Override
    public Observable<R> record(IScribe<R> scribe, ScribeLedger<R> ledger) {
        List<BsonDocument> audits = new LinkedList<>();
        ledger.forEachEntry(entry -> {
            entry
                    .getRecords()
                    .forEach(record -> record.accept(m_serializer));
            BsonDocument audit = m_serializer.getDocument().append("auditTimestamp", new BsonDateTime(entry.getTime().toEpochSecond(ZoneOffset.UTC)));
            audits.add(audit);
        });

        return m_collection.insertMany(audits).map(success -> ledger.write(scribe));

    }
}
