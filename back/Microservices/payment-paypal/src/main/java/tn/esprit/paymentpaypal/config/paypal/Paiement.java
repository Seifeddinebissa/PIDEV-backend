package tn.esprit.paymentpaypal.config.paypal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String currency;
    private String paymentMethod;
    private Date date;
    private String paymentId; // PayPal payment ID
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING; // Default to PENDING

    public Paiement(Double amount, String currency, String paymentMethod, Date date, String paymentId) {
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.paymentId = paymentId;
        this.status = PaymentStatus.PENDING;
    }
}