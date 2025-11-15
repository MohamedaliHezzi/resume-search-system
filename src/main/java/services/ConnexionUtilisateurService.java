package services;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import model.Utilisateur;
import repository.UtilisateurRepository;

@Service
public class ConnexionUtilisateurService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // Inject BCryptPasswordEncoder

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByEmail(email);
        Utilisateur user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(user.getMotDePasse())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    public boolean authenticateUser(String email, String password) {
        Optional<Utilisateur> userOptional = utilisateurRepository.findByEmail(email);
        Utilisateur user = userOptional.orElseThrow(() -> new BadCredentialsException("Invalid email/password supplied"));

        // Hash the provided password for comparison
        String hashedPassword = user.getMotDePasse();
        if (passwordEncoder.matches(password, hashedPassword)) {
            if ("approuvée".equalsIgnoreCase(user.getStatus())) {
                UserDetails userDetails = loadUserByUsername(email);
                return true;
            } else {
                throw new BadCredentialsException("User is not approved yet");
            }
        } else {
            throw new BadCredentialsException("Invalid email/password supplied");
        }
    }

    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public void resetPassword(String email) {
        // Rechercher l'utilisateur par e-mail
        Optional<Utilisateur> userOptional = utilisateurRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Utilisateur user = userOptional.get();

            // Générer un nouveau mot de passe
            String newPassword = generateRandomPassword(8);

            // Hash the new password
            String hashedPassword = passwordEncoder.encode(newPassword);

            // Mettre à jour le mot de passe de l'utilisateur dans la base de données
            user.setMotDePasse(hashedPassword);
            utilisateurRepository.save(user);

            // Envoyer un e-mail avec le nouveau mot de passe
            String subject = "Réinitialisation de mot de passe";
            String body = "Votre nouveau mot de passe est: " + newPassword;
            emailSenderService.sendSimpleEmail(email, subject, body);
        } else {
            // Gérer le cas où l'e-mail n'est pas trouvé
            throw new UsernameNotFoundException("Utilisateur non trouvé avec cet email: " + email);
        }
    }
}
