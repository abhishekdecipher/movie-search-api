package com.moviesearch.api.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviesearch.api.domain.MovieDetails;
import com.moviesearch.api.exception.SearchException;
import com.moviesearch.api.properties.ApiProperties;
import com.moviesearch.api.rest.response.MovieDetailsResponse;
import com.moviesearch.api.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Search Service Class
 *
 * @impl : it calls another rest api to get the list of movies, according to the keyword passed
 */
@Service
public class SearchServiceImpl implements SearchService {
    private static final Logger LOGGER = LogManager.getLogger(SearchServiceImpl.class);
    private ApiProperties apiProperties;
    private RestTemplate restTemplate;
    private ExecutorService executorService;

    @Autowired
    public SearchServiceImpl(ApiProperties apiProperties, @Lazy RestTemplate restTemplate) {
        this.apiProperties = apiProperties;
        this.restTemplate = restTemplate;
        executorService = Executors.newFixedThreadPool(5);
    }

    /**
     * @param keyword : keyword
     * @return : moviedetailsresponse class
     * @implNote : this method calls the rest api to get the resulted movies list and prepare the results to return
     */
    @Override
    public MovieDetailsResponse getSearchResults(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            throw new SearchException("Search Keyword Must Not Be Empty");
        }
        String uri = apiProperties.getUri().concat("&type=").concat(apiProperties.getType()).concat("&s=").concat(keyword)
                .concat("&page=");

        List<MovieDetails> movieDetailsList = new ArrayList<>();
        List<Callable<ResponseEntity<String>>> callableTasks = new ArrayList<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);

        int page = 1;
        while (page < 3) {
            int finalPage = page;
            Callable<ResponseEntity<String>> callableTask = () ->
                    restTemplate.exchange(uri + finalPage, HttpMethod.GET, httpEntity, String.class);
            callableTasks.add(callableTask);
            page++;
        }


        List<Future<ResponseEntity<String>>> futures = null;
        try {
            futures = executorService.invokeAll(callableTasks);
            for (Future<ResponseEntity<String>> future : futures) {
                prepareResult(future.get(), movieDetailsList);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new SearchException(e.getMessage());
        }


        MovieDetailsResponse response = new MovieDetailsResponse();
        response.setMovieDetailsList(movieDetailsList);
        response.setTotalResults(movieDetailsList.size());
        return response;
    }

    /**
     * @param responseEntity   : responseEntity
     * @param movieDetailsList : list of movies
     * @impl: it prepare data from json string
     */
    private void prepareResult(ResponseEntity<String> responseEntity, List<MovieDetails> movieDetailsList) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
            if (!checkResponse(rootNode)) {
                MovieDetails[] jsonObj = objectMapper.treeToValue(rootNode.get("Search"), MovieDetails[].class);
                for (MovieDetails json : jsonObj) {
                    movieDetailsList.add(json);
                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new SearchException(e.getMessage());
        }
    }

    /**
     * @param rootNode : root node of json string
     * @impl : it check for root node "search" in json string
     */
    private boolean checkResponse(JsonNode rootNode) {
        return rootNode.get("Search") == null;
    }
}
