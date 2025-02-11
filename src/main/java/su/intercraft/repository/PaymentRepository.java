package su.intercraft.repository;

import su.intercraft.model.Payment;
import su.intercraft.model.Player;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final DataSource dataSource = DataSourceConfig.getDataSource();
    public void addPayment(Payment payment) throws SQLException {
        String query = "INSERT INTO payments (player_id, amount, comment, payment_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, payment.getPlayerId());
            preparedStatement.setBigDecimal(2, payment.getAmount());
            if (payment.getComment() != null && !payment.getComment().isEmpty()) {
                preparedStatement.setString(3, payment.getComment());
            } else {
                preparedStatement.setNull(3, Types.VARCHAR);
            }
            preparedStatement.executeUpdate();
        }
    }
    public List<Payment> getAllPaymentsByPlayerId(int playerId) {
        List<Payment> paymentList = new ArrayList<>();
        String query = "SELECT * FROM payments WHERE player_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment();
                payment.setPaymentId(resultSet.getInt("payment_id"));
                payment.setPlayerId(resultSet.getInt("player_id"));
                payment.setAmount(resultSet.getBigDecimal("amount"));
                payment.setComment(resultSet.getString("comment"));
                payment.setPaymentDate(resultSet.getTimestamp("payment_date"));
                paymentList.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentList;
    }

    public BigDecimal getBalanceById(int player_id) {
        String query = "SELECT SUM(amount) FROM payments WHERE player_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, player_id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getBigDecimal(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    public void deletePaymentById(int paymentId) {
        String query = "DELETE FROM payments WHERE payment_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, paymentId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
