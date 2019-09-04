package com.moviesearch.api.service.impl;

import com.moviesearch.api.rest.SearchController;
import com.moviesearch.api.rest.response.MovieDetailsResponse;
import com.moviesearch.api.util.JsonUtil;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchServiceImplTest {

    private SearchController searchController;
    private SearchServiceImpl searchService;
    private MockMvc mockMvc;
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        searchService = mock(SearchServiceImpl.class);
        searchController = spy(new SearchController(searchService));
        restTemplate = mock(RestTemplate.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    public void getSearchResults() throws Exception {
        MovieDetailsResponse response = new MovieDetailsResponse();
        response.setTotalResults(1);
        response.setMovieDetailsList(Lists.newArrayList());
        when(searchService.getSearchResults("guardians")).thenReturn(response);
        this.mockMvc.perform(get("/api/search/").param("keyword", "guardians")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .content("guardians")
                .content(JsonUtil.toJson(response))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
