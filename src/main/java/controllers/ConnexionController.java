package controllers;

import model.EmailRequest;
import model.ResetPasswordRequest;
import model.Utilisateur;
import model.loginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import services.ConnexionUtilisateurService;
import services.EmailSenderService;
import services.UtilisateurService;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
public class ConnexionController {

    @Autowired
    private ConnexionUtilisateurService connexionUtilisateurService;
    @Autowired
    private EmailSenderService emailSenderService;

    private final UtilisateurService utilisateurService;

    public ConnexionController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/login")
    public ResponseEntity<Utilisateur> login(@RequestBody loginForm loginForm) {
        String email = loginForm.getEmail();
        String password = loginForm.getPassword();

        // Log pour vérifier les données reçues depuis le frontend
        System.out.println("Email reçu depuis le frontend : " + email);
        System.out.println("Mot de passe reçu depuis le frontend : " + password);

        try {
            boolean isAuthenticated = connexionUtilisateurService.authenticateUser(email, password);
            if (isAuthenticated) {
                Optional<Utilisateur> optionalUtilisateur = utilisateurService.getByEmail(email);

                if (optionalUtilisateur.isPresent()) {
                    return ResponseEntity.ok(optionalUtilisateur.get());
                }

            } else {
                throw new BadCredentialsException("Email ou mot de passe incorrect");
            }
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        throw new BadCredentialsException("Email ou mot de passe incorrect");

    }

    @PostMapping("/sendEmail")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest emailRequest) {
        String toEmail = emailRequest.getToEmail();
        String subject = emailRequest.getSubject();
        String body = emailRequest.getBody();

        // Envoi de l'e-mail
        emailSenderService.sendSimpleEmail(toEmail, subject, body);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        connexionUtilisateurService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

}
