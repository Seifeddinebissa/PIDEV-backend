package tn.esprit.paymentpaypal.config.paypal;

import lombok.Data;

@Data
public class PaymentResponse {
    private String approvalUrl;
    private String paymentId;
    private String errorMessage;
}
