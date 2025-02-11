package su.intercraft.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class Payment {
    private int paymentId;

    private int playerId;
    private BigDecimal amount;
    private String comment;
    private Timestamp paymentDate;
}
