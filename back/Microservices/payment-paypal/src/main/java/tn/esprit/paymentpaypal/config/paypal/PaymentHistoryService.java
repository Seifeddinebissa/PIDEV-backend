package tn.esprit.paymentpaypal.config.paypal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentHistoryService {
    private final PaiementRepository paymentHistoryRepository;

    public void savePayment(Paiement paiement) {
        paymentHistoryRepository.save(paiement);
    }

    public Paiement findByPaymentId(String paymentId) {
        return paymentHistoryRepository.findByPaymentId(paymentId).orElse(null);
    }

    public List<Paiement> getAllPayments() {
        return paymentHistoryRepository.findAll();
    }
}

