package su.intercraft.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class PlayerSkin {
    private int playerSkinId;

    private int playerId;
    private int skinId;
    private Timestamp activeTimestamp;
}
