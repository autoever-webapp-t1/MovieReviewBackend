package com.movie.MovieReview.post.excepiton;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {super("게시물이 존재하지 않습니다.");}
    public PostNotFoundException(String message) {super(message);}
}
