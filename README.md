# Back-end: CV / Document Indexing Service (Spring Boot)

Short description
This repository implements a Spring Boot back-end that:
- provides user management (registration, authentication, approval),
- accepts CV/document uploads, extracts text via Apache Tika, stores metadata in a JPA database,
- indexes extracted content in Elasticsearch for full-text search,
- sends emails (plain + attachments).

Architecture & key components
- Entry point: [`group.Application`](src/main/java/group/Application.java)
- REST controllers:
  - File upload / indexing: [`controllers.TestFileController`](src/main/java/controllers/TestFileController.java)
  - User management: [`controllers.UtilisateurController`](src/main/java/controllers/UtilisateurController.java)
  - Authentication & email actions: [`controllers.ConnexionController`](src/main/java/controllers/ConnexionController.java)
  - Search API: [`controllers.RequeteController`](src/main/java/controllers/RequeteController.java)
- Services:
  - User logic: [`services.UtilisateurService`](src/main/java/services/UtilisateurService.java)
  - Authentication, password reset: [`services.ConnexionUtilisateurService`](src/main/java/services/ConnexionUtilisateurService.java)
  - Tika extraction: [`services.ServiceTika`](src/main/java/services/ServiceTika.java)
  - Elasticsearch indexing: [`services.ServiceIndexerDocument`](src/main/java/services/ServiceIndexerDocument.java)
  - Elasticsearch queries: [`services.ServiceElasticRequete`](src/main/java/services/ServiceElasticRequete.java)
  - Email sending: [`services.EmailSenderService`](src/main/java/services/EmailSenderService.java)
- Persistence: Spring Data JPA repositories
  - [`repository.UtilisateurRepository`](src/main/java/repository/UtilisateurRepository.java)
  - [`repository.CVUploadRepository`](src/main/java/repository/CVUploadRepository.java)
  - [`repository.OffreAppelRepository`](src/main/java/repository/OffreAppelRepository.java)
- Domain models: [`model.Utilisateur`](src/main/java/model/Utilisateur.java), [`model.CvUpload`](src/main/java/model/CvUpload.java), [`model.OffreAppel`](src/main/java/model/OffreAppel.java)
- Config:
  - Elasticsearch client: [`configuration.ElasticsearchConfig`](src/main/java/configuration/ElasticsearchConfig.java)
  - Password encoder bean: [`configuration.SecurityConfig`](src/main/java/configuration/SecurityConfig.java)
- Application properties: [src/main/resources/application.properties](src/main/resources/application.properties)
- Build: [pom.xml](pom.xml)

Requirements
- Java 17
- Maven
- Running services:
  - MySQL (configured in application.properties) or adjust datasource
  - Elasticsearch (credentials/host configured in [`configuration.ElasticsearchConfig`](src/main/java/configuration/ElasticsearchConfig.java))

Quick start (local)
1. Edit database and mail settings in [src/main/resources/application.properties](src/main/resources/application.properties).
2. Start Elasticsearch and MySQL.
3. Build and run:
   mvn clean package
   mvn spring-boot:run
Or run the main class [`group.Application`](src/main/java/group/Application.java) from your IDE.

Important endpoints (examples)
- POST /api/inscription -> register user (body: Utilisateur JSON) — controller: [`controllers.UtilisateurController`](src/main/java/controllers/UtilisateurController.java)
- POST /api/login -> authenticate (body: loginForm JSON) — controller: [`controllers.ConnexionController`](src/main/java/controllers/ConnexionController.java)
- POST /api/testfile/upload -> upload file (form-data `file`) — controller: [`controllers.TestFileController`](src/main/java/controllers/TestFileController.java)
  - Flow: file sent to configured s3 upload URL, temporary file parsed by [`services.ServiceTika`](src/main/java/services/ServiceTika.java), metadata saved via [`repository.CVUploadRepository`](src/main/java/repository/CVUploadRepository.java), content indexed with [`services.ServiceIndexerDocument`](src/main/java/services/ServiceIndexerDocument.java)
- GET /queryy/rechercheterm?keywords=word1&keywords=word2 -> search indexed documents — controller: [`controllers.RequeteController`](src/main/java/controllers/RequeteController.java)

File upload curl example
curl -X POST "http://localhost:8080/api/testfile/upload" -F "file=@/path/to/your.pdf"

Notes & tips
- Passwords are hashed with BCrypt via bean in [`configuration.SecurityConfig`](src/main/java/configuration/SecurityConfig.java).
- Elasticsearch client settings are in [`configuration.ElasticsearchConfig`](src/main/java/configuration/ElasticsearchConfig.java); adapt host/port/credentials as needed.
- Email uses Spring JavaMail settings configured in [src/main/resources/application.properties](src/main/resources/application.properties).

Where to look next
- To change indexing behavior: see [`services.ServiceIndexerDocument`](src/main/java/services/ServiceIndexerDocument.java) and [`configuration.ElasticsearchConfig`](src/main/java/configuration/ElasticsearchConfig.java).
- To update user workflows: see [`services.UtilisateurService`](src/main/java/services/UtilisateurService.java) and [`controllers.UtilisateurController`](src/main/java/controllers/UtilisateurController.java).

License
Add license text or include a LICENSE file.
