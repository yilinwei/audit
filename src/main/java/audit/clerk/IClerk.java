package audit.clerk;

import java.util.concurrent.CompletionStage;

public interface IClerk<R> {
    CompletionStage<Audit<R>> next();
    Audit<R> current();
    CompletionStage<Audit<R>> previous();
}
