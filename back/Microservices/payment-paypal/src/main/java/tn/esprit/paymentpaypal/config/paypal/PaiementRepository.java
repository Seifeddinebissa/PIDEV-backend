package tn.esprit.paymentpaypal.config.paypal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByPaymentId(String paymentId);
}
