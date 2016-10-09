package audit.scribe;

import java.time.LocalDateTime;

final class ScribeMilestone<R> {
    private final LocalDateTime m_time;
    private final R m_milestone;

    ScribeMilestone(R milestone, LocalDateTime time) {
        m_time = time;
        m_milestone = milestone;
    }

    LocalDateTime getTime() {
        return m_time;
    }

    R getMilestone() {
        return m_milestone;
    }
}
