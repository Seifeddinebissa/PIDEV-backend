package tn.esprit.paymentpaypal.config.paypal;

import lombok.Data;

@Data
public class PaymentExecutionRequest {
    private String paymentId;
    private String payerId;
}
