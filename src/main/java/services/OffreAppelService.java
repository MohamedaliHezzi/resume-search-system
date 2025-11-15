package services;

import model.OffreAppel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.OffreAppelRepository;

import java.util.List;

@Service
public class OffreAppelService {

    private final OffreAppelRepository offreAppelRepository;

    @Autowired
    public OffreAppelService(OffreAppelRepository offreAppelRepository) {
        this.offreAppelRepository = offreAppelRepository;
    }

    public OffreAppel saveOffreAppel(String email, String nom, String objet) {
        OffreAppel offreAppel = new OffreAppel(email, nom, objet);
        return offreAppelRepository.save(offreAppel);
    }

    public List<OffreAppel> getAllOffresAppel() {
        return offreAppelRepository.findAll();
    }

    public OffreAppel getOffreAppelById(Long id) {
        return offreAppelRepository.findById(id).orElse(null);
    }

    // Vous pouvez ajouter d'autres méthodes en fonction de vos besoins
}
