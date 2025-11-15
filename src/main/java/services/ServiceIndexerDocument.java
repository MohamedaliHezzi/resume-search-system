package services;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceIndexerDocument {

    @Autowired
    private RestHighLevelClient elasticsearchClient;


    public void indexDocument(String indexName, String content,String nom_de_fichier) {
    	try {
            IndexRequest request = new IndexRequest(indexName);
            request.source("content", content, "nom_de_fichier", nom_de_fichier);
            IndexResponse response = elasticsearchClient.index(request, RequestOptions.DEFAULT);
            System.out.println("Document indexé avec succès sous l'ID : " + response.getId());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'indexation du document : " + e.getMessage());
        }
    }
}


