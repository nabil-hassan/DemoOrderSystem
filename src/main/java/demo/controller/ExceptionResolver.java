package demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import demo.exception.BasketEmptyException;
import demo.exception.ItemNotInBasketException;
import demo.exception.EntityNotFoundException;
import demo.exception.InsufficientStockException;

@ControllerAdvice
public class ExceptionResolver extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler(value = { EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        LOG.error("Request exception", ex);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { ItemNotInBasketException.class, BasketEmptyException.class,
            InsufficientStockException.class })
    protected ResponseEntity<Object> handleOtherExceptions(RuntimeException ex, WebRequest request) {
        LOG.error("Request exception", ex);
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        LOG.error("Internal server error", ex);
        return handleExceptionInternal(ex, "Internal server error", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }
}
