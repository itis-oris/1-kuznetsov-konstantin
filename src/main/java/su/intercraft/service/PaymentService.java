package su.intercraft.service;

import su.intercraft.model.Payment;
import su.intercraft.repository.PaymentRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService() {
        this.paymentRepository = new PaymentRepository();
    }

    public void addPayment(Payment payment) throws SQLException {
        paymentRepository.addPayment(payment);
    }

    public void addPayment(BigDecimal amount, int playerId) throws SQLException {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPlayerId(playerId);
        paymentRepository.addPayment(payment);
    }

    public List<Payment> getAllPaymentsByPlayerId(int playerId) {
        return paymentRepository.getAllPaymentsByPlayerId(playerId);
    }

    public void deletePaymentById(int paymentId) {
        paymentRepository.deletePaymentById(paymentId);
    }

    public BigDecimal getBalanceById(int playerId) {
        return paymentRepository.getBalanceById(playerId);
    }
}
