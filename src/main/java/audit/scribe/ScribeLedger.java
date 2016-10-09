package audit.scribe;

import audit.record.IRecord;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.function.Consumer;

public class ScribeLedger<R> {

    //TODO: Add the last entry for existing one
    private final LinkedList<ScribeEntry> m_entries = new LinkedList<>();
    private final LinkedList<ScribeMilestone<R>> m_milestones = new LinkedList<>();

    public void newRecord(IRecord record) {
        if(m_entries.isEmpty()) {
            throw new IllegalStateException("unable to add record when no time is specified");
        } else {
            m_entries.getLast().newRecord(record);
        }
    }

    public void newEntry(LocalDateTime time) {
        m_entries.add(new ScribeEntry(time));
    }

    public void newMilestone(LocalDateTime time, IScribe<R> scribe) {
        scribe.begin(getClosestMilestone(time));
        forEachEntry(time, entry -> entry.getRecords().forEach(scribe::dictate));
        newMilestone(time, scribe.write());
    }

    public void newMilestone(LocalDateTime time, R milestone) {
        m_milestones.add(new ScribeMilestone<R>(milestone, time));
    }


    R getClosestMilestone(LocalDateTime time) {
        return null;
    }

    public void forEachEntry(LocalDateTime time, Consumer<ScribeEntry> consumer) {

    }

    public void forEachEntry(Consumer<ScribeEntry> consumer) {

    }

    public R write(LocalDateTime time, IScribe<R> scribe) {
        scribe.begin(getClosestMilestone(time));
        forEachEntry(time, entry -> entry.getRecords().forEach(scribe::dictate));
        return scribe.write();
    }

    public R write(IScribe<R> scribe) {
        ScribeMilestone<R> milestone = m_milestones.getLast();
        return write(milestone.getTime(), scribe);
    }
}
