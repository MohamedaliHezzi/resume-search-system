package controllers;

import model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.UtilisateurService;

import java.util.List;


@RequestMapping(value = "/api", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@RestController
public class UtilisateurController {
    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/inscription")
    public ResponseEntity<String> inscriptionUtilisateur(@RequestBody Utilisateur utilisateur) {
        utilisateurService.inscrireUtilisateur(utilisateur);
        return ResponseEntity.status(HttpStatus.OK).body("Inscription réussie");
    }

    @GetMapping("/existe")
    public ResponseEntity<String> utilisateurExiste(@RequestParam String email) {
        boolean utilisateurExiste = utilisateurService.utilisateurExiste(email);
        String response = utilisateurExiste ? "true" : "false";
        return ResponseEntity.ok(response);

    }

    @GetMapping("/getUtilisateur")
    public List<Utilisateur> getUtilisateurs() {
        return utilisateurService.getUtilisateurs();
    }

    @GetMapping("/get-utilisateur/{id}")
    public Utilisateur getUtilisateurs(@PathVariable Long id) {
        return utilisateurService.getUserById(id);
    }

    @DeleteMapping("/deleteutilisateur")
    public void deleteUtilisateur(@RequestParam Long id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @PutMapping("/updateUtilisateur/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur utilisateur) {
        Utilisateur updatedUtilisateur = utilisateurService.updateUtilisateur(id, utilisateur);
        if (updatedUtilisateur != null) {
            return ResponseEntity.ok(updatedUtilisateur);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/approuverUtilisateur/{idUtilisateur}")
    public ResponseEntity<String> approuverUtilisateur(@PathVariable Long idUtilisateur) {
        try {
            utilisateurService.approuverUtilisateur(idUtilisateur);
            return ResponseEntity.ok("Utilisateur approuvé avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/utilisateurs/enattente")
    public ResponseEntity<List<Utilisateur>> getUtilisateursEnAttente() {
        List<Utilisateur> utilisateursEnAttente = utilisateurService.getUtilisateursEnAttente();
        return ResponseEntity.ok().body(utilisateursEnAttente);
    }


}
