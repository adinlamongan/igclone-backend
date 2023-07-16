package com.adn.cloneig.advice;

import com.adn.cloneig.exceptions.ResourceNotFoundException;
import com.adn.cloneig.utils.ErrorRespone;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                     WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ErrorRespone errorRespone = new ErrorRespone();
        errorRespone.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorRespone.setError("Not found");
        errorRespone.setPath(servletRequest.getRequestURI());
        errorRespone.setMessage(errors);
        return new ResponseEntity<>(errorRespone, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(err -> err.unwrap(ConstraintViolation.class))
                .map(err -> camelToSnake(String.valueOf(err.getPropertyPath())) + " "
                        + err.getMessage())
                .collect(Collectors.toList());

        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ErrorRespone errorRespone = new ErrorRespone();
        errorRespone.setStatus(HttpStatus.BAD_REQUEST.value());
        errorRespone.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorRespone.setPath(servletRequest.getRequestURI());
        errorRespone.setMessage(errors);
        return new ResponseEntity<>(errorRespone, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<Object> handleInvalidTypeDataInput(InvalidFormatException ex, WebRequest request) {
        List<String> errors = ex.getPath().stream()
                .map(err -> camelToSnake(err.getFieldName()) + " must be "
                        + ex.getTargetType().getSimpleName())
                .collect(Collectors.toList());
        HttpServletRequest servletRequest = ((ServletWebRequest) request).getRequest();
        ErrorRespone errorRespone = new ErrorRespone();
        errorRespone.setStatus(HttpStatus.BAD_REQUEST.value());
        errorRespone.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorRespone.setPath(servletRequest.getRequestURI());
        errorRespone.setMessage(errors);
        return new ResponseEntity<>(errorRespone, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private static String camelToSnake(String str) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        str = str.replaceAll(regex, replacement).toLowerCase();
        return str;
    }
}
