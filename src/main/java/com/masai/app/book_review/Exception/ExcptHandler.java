package com.masai.app.book_review.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice
public class ExcptHandler extends ResponseEntityExceptionHandler {
    public final ResponseEntity<Object> userNotFound (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), e.getMessage(), request.getDescription(false));
        return  new ResponseEntity<>(exceptionResponse,HttpStatus.ACCEPTED);
    }
    public final ResponseEntity<Object> DataInConsistency (Exception e, WebRequest request){
        ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(), e.getMessage(), request.getDescription(false));
        return  new ResponseEntity<>(exceptionResponse,HttpStatus.EXPECTATION_FAILED);
    }
}
