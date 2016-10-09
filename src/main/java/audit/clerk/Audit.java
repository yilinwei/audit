package audit.clerk;

import java.time.LocalDateTime;

public interface Audit<R> {
    R peek();
    LocalDateTime getTime();
}
