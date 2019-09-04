package com.moviesearch.api.service;

import com.moviesearch.api.rest.response.MovieDetailsResponse;

/**
 * SerachService interface
 */
public interface SearchService {

    MovieDetailsResponse getSearchResults(String keyword);
}
