package com.bloggingapp.exceptions;


public class CommentNotFoundException extends RuntimeException{

    public CommentNotFoundException(String msg){
        super(msg);
    }

}
