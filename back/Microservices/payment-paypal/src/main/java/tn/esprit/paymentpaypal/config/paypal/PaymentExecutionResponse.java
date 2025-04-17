package tn.esprit.paymentpaypal.config.paypal;

import lombok.Data;

@Data
public class PaymentExecutionResponse {
    private String message;
    private String paymentId;
}
