package model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.Id;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity
@Table(name = "cv_upload")
public class CvUpload {

	
	
		    
		@Override
	public String toString() {
		return "CvUpload [idCv=" + idCv + ", utilisateur=" + utilisateur + ", nomFichier=" + nomFichier
				+ ", dateUpload=" + dateUpload + ", typeDocument=" + typeDocument + "]";
	}

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long idCv;

	    @ManyToOne
	    @JoinColumn(name = "id_utilisateur")
	    private Utilisateur utilisateur;

	    private String nomFichier;
	    private LocalDateTime dateUpload;
	    
	    @Enumerated(EnumType.STRING)
	    private TypeDocument typeDocument;
		public Long getIdCv() {
			return idCv;
		}
		public void setIdCv(Long idCv) {
			this.idCv = idCv;
		}
		public Utilisateur getUtilisateur() {
			return utilisateur;
		}
		public void setUtilisateur(Utilisateur utilisateur) {
			this.utilisateur = utilisateur;
		}
		public String getNomFichier() {
			return nomFichier;
		}
		public void setNomFichier(String nomFichier) {
			this.nomFichier = nomFichier;
		}
		public LocalDateTime getDateUpload() {
			return dateUpload;
		}
		public void setDateUpload(LocalDateTime dateUpload) {
			this.dateUpload = dateUpload;
		}
		public void setTypeDocument(TypeDocument typeDocument) {
			this.typeDocument = typeDocument;
		}
		
		public TypeDocument getTypeDocument() {
			return typeDocument;
		}
		
	    
	    
	    
}