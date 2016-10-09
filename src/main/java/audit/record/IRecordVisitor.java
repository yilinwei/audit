package audit.record;

/**
 * @author yilin
 * @see audit.record.IRecord
 *
 * Visitor for the IRecord
 */
public interface IRecordVisitor {
    void visit(StringRecord record);
    void visit(IntRecord record);
}

