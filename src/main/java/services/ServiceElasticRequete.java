package services;
import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceElasticRequete {

    @Autowired
    private RestHighLevelClient elasticsearchClient;


    public SearchResponse rechercheparterm(List<String> keywords) throws IOException {
        SearchRequest rechreq = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(String.join(" ", keywords))
                .field("content"); 

        searchSourceBuilder.query(multiMatchQuery);
        rechreq.source(searchSourceBuilder);

        return elasticsearchClient.search(rechreq, RequestOptions.DEFAULT);
    }

    public String getDocumentContentById( String index,String id) throws IOException {
        GetRequest getRequest = new GetRequest(index,id);
        GetResponse getResponse = elasticsearchClient.get(getRequest, RequestOptions.DEFAULT);
        
        if (getResponse.isExists()) {
            String content = getResponse.getSourceAsString();
            return content;
        } else {
            return null; 
        }
    }
/*
    public SearchResponse recherchparrang( String field, String from, String to) throws IOException {
        SearchRequest rechreq1 = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            RangeQueryBuilder requeterange = QueryBuilders.rangeQuery(field)
                .gte(from)
                .lte(to);

        searchSourceBuilder.query(requeterange);
        rechreq1.source(searchSourceBuilder);
        return elasticsearchClient.search(rechreq1, RequestOptions.DEFAULT);
    }


    public SearchResponse recherchemuchrequete(String field, String value) throws IOException {
        SearchRequest requetrech = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(field, value));
        requetrech.source(searchSourceBuilder);
        return elasticsearchClient.search(requetrech, RequestOptions.DEFAULT);
    }

    public SearchResponse rechercheFuzzyRequete( String field, String value) throws IOException {
        SearchRequest rechrcherequet= new SearchRequest();
        SearchSourceBuilder searchsourcebuilder= new SearchSourceBuilder();
        searchsourcebuilder.query(QueryBuilders.matchQuery(field , value ));
        rechrcherequet.source(searchsourcebuilder);
        return elasticsearchClient.search(rechrcherequet,RequestOptions.DEFAULT);


    }
    public SearchResponse rechercheWildcardRequete( String Field ,String Willcard) throws IOException{
    	SearchRequest  requetrech= new SearchRequest();
    	SearchSourceBuilder searchsourcebuilder= new SearchSourceBuilder();
    	searchsourcebuilder.query(QueryBuilders.wildcardQuery(Field, Willcard));

    	requetrech.source(searchsourcebuilder);
    	return elasticsearchClient.search(requetrech, RequestOptions.DEFAULT);

    }*/

}


