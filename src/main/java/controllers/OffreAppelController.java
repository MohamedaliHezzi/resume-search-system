package controllers;

import model.OffreAppel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.OffreAppelRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/offres")
public class OffreAppelController {

    @Autowired
    private OffreAppelRepository repository;

    @GetMapping
    public List<OffreAppel> getAllOffres() {
        return repository.findAll();
    }

    @PostMapping
    public OffreAppel createOffre(@RequestBody OffreAppel offreAppel) {
        offreAppel.setDateSoumission(LocalDateTime.now());
        return repository.save(offreAppel);
    }
}
