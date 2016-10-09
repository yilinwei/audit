package audit.record;

/**
 * @author yilin
 *
 * Represents a change in a string field
 */
public final class StringRecord implements IRecord {
    
    private final String m_field;
    private final String m_value;

    public StringRecord(String field, String value) {
        m_field = field;
        m_value = value;
    }

    public String getField() {
        return m_field;
    }

    public String getValue() {
        return m_value;
    }

    @Override
    public void accept(IRecordVisitor visitor) {
        visitor.visit(this);
    }
}
