package controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import model.CvUpload;
import model.TypeDocument;
import repository.CVUploadRepository;
import services.ServiceIndexerDocument;
import services.ServiceTika;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value="/api/testfile", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
public class TestFileController {

    @Autowired
    private ServiceTika tikaService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${s3.uploadUrl}")
    private String s3UploadUrl;

    @Autowired
    private CVUploadRepository cvUploadRepository;

    @Autowired
    private ServiceIndexerDocument documentIndexer;

    @PostMapping("/upload")
    public ResponseEntity<Object> testfile(@RequestParam("file") MultipartFile file) throws TikaException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        String fileName1 = file.getOriginalFilename();
        body.add("nom_de_fichier", fileName1);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(s3UploadUrl, HttpMethod.POST, requestEntity, String.class);

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                File tempFile = convertMultipartFileToFile(file);
                String fileName = file.getOriginalFilename();
                String content = tikaService.extractContent(tempFile);

                CvUpload cvUpload = new CvUpload();
                cvUpload.setNomFichier(file.getOriginalFilename());
                cvUpload.setDateUpload(LocalDateTime.now());
                String fileExtension = StringUtils.getFilenameExtension(fileName);
                TypeDocument typeDocument = TypeDocument.fromFileExtension(fileExtension);
                cvUpload.setTypeDocument(typeDocument);

                cvUploadRepository.save(cvUpload);
                documentIndexer.indexDocument("test", content, fileName);

                HttpHeaders successHeaders = new HttpHeaders();
                successHeaders.setContentType(MediaType.TEXT_PLAIN);
                return ResponseEntity.ok().headers(successHeaders).body("Fichier a été uploadé avec succès vers S3");
            } else {
                HttpHeaders errorHeaders = new HttpHeaders();
                errorHeaders.setContentType(MediaType.TEXT_PLAIN);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(errorHeaders).body("Erreur lors de l'upload du fichier vers S3");
            }
        } catch (IOException ex) {
            HttpHeaders errorHeaders = new HttpHeaders();
            errorHeaders.setContentType(MediaType.TEXT_PLAIN);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(errorHeaders).body("Erreur lors de l'upload du fichier");
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        return tempFile;
    }
}
