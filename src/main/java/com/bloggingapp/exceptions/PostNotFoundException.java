package com.bloggingapp.exceptions;


public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(String msg){
        super(msg);
    }

}
