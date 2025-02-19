package tn.esprit.gestionpaiementnotification.services;


import tn.esprit.gestionpaiementnotification.entities.Paiement;

import java.util.List;

public interface PaiementService {
    Paiement addPaiement(Paiement paiement);
    Paiement updatePaiement(Paiement paiement);
    List<Paiement> getAllPaiements();
    void deletePaiement(Long id);
    Paiement getPaiement(Long id);

}
