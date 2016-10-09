package audit.scribe;

import audit.record.IntRecord;
import audit.record.IRecord;
import audit.record.IRecordVisitor;
import audit.record.StringRecord;

public interface IBuilderScribe<R, B> extends IScribe<R>, IRecordVisitor {
    
    void setBuilder(B builder);
    B getBuilder();

    B newBuilder(R milestone);
    R build(B builder);

    @Override
    default void begin(R milestone) {
        setBuilder(newBuilder(milestone));
    }

    @Override
    default void dictate(IRecord record) {
        record.accept(this);
    }

    @Override
    default R write() {
        return build(getBuilder());
    }
           
    default void visit(StringRecord record) {
        throw new IllegalArgumentException("unknown field " + record.toString());
    }

    default void visit(IntRecord record) {
        throw new IllegalArgumentException("unknown field " + record.toString());
    }
}
