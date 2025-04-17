package tn.esprit.paymentpaypal.config.paypal;

import lombok.Data;

@Data
public class PaymentRequest {
    private Double amount;
    private String currency;
    private String description;
    private String cancelUrl;
    private String successUrl;
}
