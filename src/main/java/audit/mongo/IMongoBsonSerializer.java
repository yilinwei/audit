package audit.mongo;

import audit.record.IRecord;
import audit.record.IRecordVisitor;
import audit.record.IntRecord;
import audit.record.StringRecord;
import org.bson.*;

public interface IMongoBsonSerializer<R> extends IRecordVisitor {

    @Override
    default void visit(StringRecord record) {
        getDocument().append(record.getField(), new BsonString(record.getValue()));
    }

    @Override
    default void visit(IntRecord record) {
        getDocument().append(record.getField(), new BsonInt32(record.getValue()));
    }

    BsonDocument setIdentifier(R object, BsonDocument document);
    BsonDocument getDocument();
    void setDocument(BsonDocument document);

    default BsonDocument write(R object, IRecord record) {
        setDocument(setIdentifier(object, new BsonDocument()));
        record.accept(this);
        return getDocument();
    }

    BsonDocument serialize(R object);
}
