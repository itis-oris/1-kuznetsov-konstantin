package su.intercraft.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import su.intercraft.repository.PlayerSkinRepository;
import su.intercraft.repository.SkinRepository;
import su.intercraft.service.PaymentService;
import su.intercraft.service.PlayerService;
import su.intercraft.service.PlayerSkinService;
import su.intercraft.service.SkinService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

@Data
@NoArgsConstructor
public class Player {
    private int playerId;

    private String nickname;
    private String password;
    private Timestamp registrationDate;
    private Timestamp expirationDate;

    public boolean checkPassword(String password){
        return BCrypt.checkpw(password, this.password);
    }

    public Skin getActiveSkin() {
        PlayerSkinService service = new PlayerSkinService();
        return service.getActiveSkin(this.playerId);
    }

    public List<Skin> getAllSkins() throws SQLException {
        SkinService service = new SkinService();
        return service.getAllSkinsByPlayerId(this.playerId);
    }

    public String getStringBalance() {
        DecimalFormat df = new DecimalFormat("0.00");
        PaymentService service = new PaymentService();
        return df.format(service.getBalanceById(this.playerId));
    }

    public double getDoubleBalance() {
        PaymentService service = new PaymentService();
        BigDecimal balance = service.getBalanceById(this.playerId);
        return balance.doubleValue();
    }

    public String getExpirationTime() {
        PlayerService service = new PlayerService();
        return service.calculateExpirationDifference(expirationDate);
    }
}
