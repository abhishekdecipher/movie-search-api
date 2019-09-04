package com.moviesearch.api.rest;

import com.moviesearch.api.domain.MovieDetails;
import com.moviesearch.api.rest.response.MovieDetailsResponse;
import com.moviesearch.api.service.impl.SearchServiceImpl;
import com.moviesearch.api.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchControllerTest {

    private SearchController searchController;
    private SearchServiceImpl searchService;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        searchService = mock(SearchServiceImpl.class);
        searchController = spy(new SearchController(searchService));
        this.mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    public void getSearchResult() throws Exception {
        List<MovieDetails> movieDetailsList = new ArrayList<>();
        MovieDetails movieDetails = new MovieDetails();
        movieDetails.setTitle("LEGO Marvel Super Heroes - Guardians of the Galaxy: The Thanos Threat");
        movieDetails.setYear("2017");
        movieDetails.setType("movie");
        movieDetails.setPoster("https://m.media-amazon.com/images/");
        movieDetails.setImdbId("tt7387224");
        movieDetailsList.add(movieDetails);

        MovieDetailsResponse response = new MovieDetailsResponse();
        response.setTotalResults(movieDetailsList.size());
        response.setMovieDetailsList(movieDetailsList);
        when(searchController.getSearchResult("guardians")).thenReturn(response);
        this.mockMvc.perform(get("/api/search/").param("keyword", "guardians")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("guardians")
                .content(JsonUtil.toJson(response))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
