package audit.record;

/**
 * @author yilin
 *
 * Represents a change in an integer field.
 */
public final class IntRecord implements IRecord {
    
    private final String m_field;
    private final int m_value;

    public IntRecord(String field, int value) {
        m_field = field;
        m_value = value;
    }

    public String getField() {
        return m_field;
    }

    public int getValue() {
        return m_value;
    }

    @Override
    public void accept(IRecordVisitor visitor) {
        visitor.visit(this);
    }
}
