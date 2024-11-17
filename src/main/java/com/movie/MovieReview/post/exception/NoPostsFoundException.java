package com.movie.MovieReview.post.exception;

public class NoPostsFoundException extends RuntimeException{
    public NoPostsFoundException(String message) {
        super(message);
    }
}
