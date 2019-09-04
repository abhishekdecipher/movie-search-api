package com.moviesearch.api.rest.response;

import com.moviesearch.api.domain.MovieDetails;

import java.util.Arrays;
import java.util.List;

/**
 * Movie Details Response Class
 */
public class MovieDetailsResponse {

    private List<MovieDetails> movieDetailsList = Arrays.asList();
    private int totalResults;

    public List<MovieDetails> getMovieDetailsList() {
        return movieDetailsList;
    }

    public void setMovieDetailsList(List<MovieDetails> movieDetailsList) {
        this.movieDetailsList = movieDetailsList;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
