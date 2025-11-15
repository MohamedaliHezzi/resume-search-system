package controllers;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ServiceElasticRequete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/queryy", method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
public class RequeteController {


    @Autowired
    private ServiceElasticRequete elasticsearchService;

    @GetMapping("/rechercheterm")
    public List<Map<String, String>> searchDocuments(@RequestParam List<String> keywords) throws IOException {
        SearchResponse response = elasticsearchService.rechercheparterm(keywords);
        SearchHit[] hits = response.getHits().getHits();
        List<Map<String, String>> documents = new ArrayList<>();


        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap();
            String nom = (String) source.get("nom_de_fichier");
            String contenu = (String) source.get("content");
            String id = hit.getId();

            Map<String, String> document = new HashMap<>();
            document.put("id", id);
            document.put("nom", nom);
            document.put("contenu", contenu);

            documents.add(document);
        }


        return documents;
    }

    @GetMapping("/documents")
    public ResponseEntity<Object> getDocumentContentById(@RequestParam String index, @RequestParam String id) {
        try {
            String content = elasticsearchService.getDocumentContentById("test", id);
            if (!content.isEmpty()) {

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("content", content);
                // Vous pouvez également inclure d'autres propriétés si nécessaire
                return ResponseEntity.ok(responseData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            // Gérer les erreurs d'Elasticsearch ici
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération du contenu du document");
        }
    }
   /*   SearchResponse response = elasticsearchService.rechercheparterm(keywords);

		    	    SearchHit[] hits = response.getHits().getHits();

		    	    List<String> relevantContent = new ArrayList<>();

		    	    for (SearchHit hit : hits) {
		    	        Map<String, Object> source = hit.getSourceAsMap();
		    	        String content = (String) source.get("content");
		    	        relevantContent.add(content);
		    	    }

		    	    return relevantContent;}*/
	    	    


      /* @GetMapping("/rechercherange")
       public SearchResponse rangeQuerySearch(String field, String from, String to) throws IOException {
    	   return elasticsearchService.recherchparrang( field, from, to);

    	   }


       @GetMapping("/recherchemuch")
      public SearchResponse recherchemuchrequete( String field, String value) throws IOException {
   	   return elasticsearchService.recherchemuchrequete( field, value);

   	   }
       @GetMapping("/rechrcheFuzzy")
       public  SearchResponse rechrcherechercheFuzzyRequete( String Field,String value) throws IOException{
    	   return elasticsearchService.rechercheFuzzyRequete( Field, value);

       }*/

}






