package com.moviesearch.api.rest;


import com.moviesearch.api.rest.response.MovieDetailsResponse;
import com.moviesearch.api.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Search Controller
 *
 * @purpose : To get the search results of movies using any keyword
 */
@RestController
@CrossOrigin
public class SearchController {

    private SearchService searchService;

    /**
     * @param searchService :search Service
     */
    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * Rest Template Bean
     *
     * @param builder : builder
     * @return
     */
    @Bean
    public RestTemplate restTemplateBean(RestTemplateBuilder builder) {
        return builder.build();
    }

    /**
     * @param keyword : serach movies based on this keyword
     * @return : moviedetailsresponse
     * @purpose: search movies based on the keyword
     */
    @GetMapping(value = "/api/search")
    public MovieDetailsResponse getSearchResult(@RequestParam("keyword") String keyword) {
        return searchService.getSearchResults(keyword);
    }
}
