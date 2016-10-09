package audit.scribe;

import audit.record.IRecord;

import java.time.LocalDateTime;

/**
 * The Scribe is used to apply each record of a change
 *
 * @param <R> record type
 */
public interface IScribe<R> {

    R peek();
    R peekAt(LocalDateTime time);

    void begin(R milestone);
    void dictate(IRecord record);
    R write();
}

