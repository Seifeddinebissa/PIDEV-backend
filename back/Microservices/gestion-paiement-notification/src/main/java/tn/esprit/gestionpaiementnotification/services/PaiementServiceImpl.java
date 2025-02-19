package tn.esprit.gestionpaiementnotification.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.gestionpaiementnotification.entities.Paiement;
import tn.esprit.gestionpaiementnotification.repositorys.PaiementRepository;

import java.util.List;

@Service
public class PaiementServiceImpl implements PaiementService {
    @Autowired
    private PaiementRepository paiementRepository;
    @Override
    public Paiement addPaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public Paiement updatePaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Override
    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    @Override
    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }

    @Override
    public Paiement getPaiement(Long id) {
        return paiementRepository.findById(id).get();
    }
}
