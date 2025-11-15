package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import model.Utilisateur;
import repository.UtilisateurRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean authenticateUser(String email, String password) {
        Utilisateur user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        // Vérifie si le mot de passe fourni correspond au mot de passe stocké
        if (passwordEncoder.matches(password, user.getMotDePasse())) {
            // Vérifie si l'utilisateur est approuvé
            if (user.isApprouve()) {
                return true; // Authentification réussie
            } else {
                throw new IllegalArgumentException("Utilisateur non approuvé");
            }
        } else {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }
    }

    public void inscrireUtilisateur(Utilisateur utilisateur) {
        if(utilisateur.getMotDePasse() == null) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être nul");
        }
        if(utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }
        // Hash the password
        String hashedPassword = passwordEncoder.encode(utilisateur.getMotDePasse());
        utilisateur.setMotDePasse(hashedPassword);

        // Autres validations à ajouter ici...

        utilisateurRepository.save(utilisateur);
    }

    public boolean utilisateurExiste(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    public List<Utilisateur> getUtilisateurs(){
        return (List<Utilisateur>) utilisateurRepository.findAll();
    }

    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id );
    }

    public Utilisateur updateUtilisateur(Long idUtilisateur, Utilisateur utilisateur) {
        Utilisateur existingUtilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        existingUtilisateur.setNomUtilisateur(utilisateur.getNomUtilisateur());
        existingUtilisateur.setEmail(utilisateur.getEmail());
        existingUtilisateur.setMotDePasse(utilisateur.getMotDePasse());
        existingUtilisateur.setStatus(utilisateur.getStatus());
        existingUtilisateur.setRole(utilisateur.getRole());
        existingUtilisateur.setApprouve(utilisateur.isApprouve());

        return utilisateurRepository.save(existingUtilisateur);
    }

    public void approuverUtilisateur(Long idUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        utilisateur.setApprouve(true); // Approuver l'utilisateur
        utilisateurRepository.save(utilisateur); // Mettre à jour l'utilisateur dans la base de données
    }

    public List<Utilisateur> getUtilisateursEnAttente() {
        // Implémentez la logique pour obtenir les utilisateurs en attente
        // Par exemple, vous pouvez interroger la base de données pour récupérer les utilisateurs en attente
        // Utilisez la méthode appropriée de votre repository pour cela
        // Retournez la liste des utilisateurs en attente
        return utilisateurRepository.findByStatus("EN_ATTENTE");
    }

    public Optional<Utilisateur> getByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public Utilisateur getUserById(Long id){
        return utilisateurRepository.findById(id).orElseThrow();

    }
}
