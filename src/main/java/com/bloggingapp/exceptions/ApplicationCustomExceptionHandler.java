package com.bloggingapp.exceptions;


import com.bloggingapp.utils.MethodUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationCustomExceptionHandler {

    @ExceptionHandler(value=ApplicationException.class)
    public ResponseEntity<String> applicationException(ApplicationException exception){
        HttpStatus status= HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(status,exception),status);
    }

    @ExceptionHandler(value=PostNotFoundException.class)
    public ResponseEntity<String> postNotFoundExceptionException(PostNotFoundException exception){
        HttpStatus status= HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(status,exception),status);
    }

    @ExceptionHandler(value=UserNotFoundException.class)
    public ResponseEntity<String> userNotFoundExceptionException(UserNotFoundException exception){
        HttpStatus status= HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(status,exception),status);
    }

    @ExceptionHandler(value=CommentNotFoundException.class)
    public ResponseEntity<String> commentNotFoundExceptionException(CommentNotFoundException exception){
        HttpStatus status= HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(MethodUtils.prepareErrorJSON(status,exception),status);
    }
}
