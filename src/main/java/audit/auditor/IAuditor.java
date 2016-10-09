package audit.auditor;

import audit.scribe.IScribe;
import audit.scribe.ScribeLedger;
import rx.Observable;

public interface IAuditor<R> {
    Observable<R> record(IScribe<R> scribe, ScribeLedger<R> ledger);
}

