package audit.record;

/**
 * @author yilin
 *
 * Represents a record of a change of one of the fields. Since the records are dynamic, we implement a visitor pattern
 * in order to allow polymorphism and abstract the structure to the individual records
 */
public interface IRecord {
    void accept(IRecordVisitor visitor);
}
