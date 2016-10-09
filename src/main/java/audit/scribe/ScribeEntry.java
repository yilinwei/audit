package audit.scribe;

import audit.record.IRecord;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public final class ScribeEntry {

    private final LocalDateTime m_time;
    private final List<IRecord> m_records = new LinkedList<>();

    ScribeEntry(LocalDateTime time) {
        m_time = time;
    }

    void newRecord(IRecord record) {
        m_records.add(record);
    }

    public List<IRecord> getRecords() {
        return m_records;
    }

    public LocalDateTime getTime() {
        return m_time;
    }
}
