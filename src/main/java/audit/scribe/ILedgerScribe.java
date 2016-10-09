package audit.scribe;

import java.time.LocalDateTime;

public interface ILedgerScribe<R> extends IScribe<R> {

    ScribeLedger<R> getLedger();

    @Override
    default R peek() {
        return getLedger().write(this);
    }

    @Override
    default R peekAt(LocalDateTime time) {
        return getLedger().write(time, this);
    }


}
