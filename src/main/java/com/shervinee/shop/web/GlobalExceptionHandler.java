package com.shervinee.shop.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String REQ_ID_HEADER_NAME = "X-Request-ID";

    // 400: bad JSON/body, etc.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleBadBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.debug("Malformed request body", ex);
        return baseProblem(request, HttpStatus.BAD_REQUEST, "Malformed request body");
    }

    // 400: @Valid on @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ProblemDetail problemDetail = baseProblem(request, HttpStatus.BAD_REQUEST, "Validation failed: ");
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }

    // 400: @Validated on @RequestParam/@PathVariable
    @ExceptionHandler({ConstraintViolationException.class, BindException.class})
    public ProblemDetail handleConstraint(Exception ex, HttpServletRequest request) {
        log.debug("Constraint violation", ex);
        return baseProblem(request, HttpStatus.BAD_REQUEST, "Constraint violation.");
    }

    // 400: wrong param type (?page=foo)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        MethodParameter parameter = ex.getParameter();
        String name = parameter != null ? parameter.getParameterName() : "unknowns";
        ProblemDetail problemDetail = baseProblem(request, HttpStatus.BAD_REQUEST, "Parameter type mismatch.");
        problemDetail.setProperty("parameter", name);
        problemDetail.setProperty("value", String.valueOf(ex.getValue()));
        return problemDetail;
    }


    // 404: your own "not found" scenarios
    @ExceptionHandler({NoSuchElementException.class, ResourceNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException ex, HttpServletRequest request) {
        log.debug("Resource not found", ex);
        return baseProblem(request, HttpStatus.NOT_FOUND, "Resource not found.");
    }

    // 403: security
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.debug("Access denied", ex);
        return baseProblem(request, HttpStatus.FORBIDDEN, "Access denied.");
    }

    // 405: wrong method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.debug("Method not allowed", ex);
        return baseProblem(request, HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed.");
    }

    // 400: generic illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArg(IllegalArgumentException ex, HttpServletRequest request) {
        log.debug("Illegal argument", ex);
        return baseProblem(request, HttpStatus.BAD_REQUEST, ex.getMessage() != null ? ex.getMessage() : "Bad request.");
    }

    // 500: last-resort
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAll(Exception ex, HttpServletRequest request) {
        // Log full stack but don’t leak it to clients
        log.error("Unhandled error on {} {}", request.getMethod(), request.getRequestURI(), ex);
        return baseProblem(request, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error.");
    }

    private ProblemDetail baseProblem(HttpServletRequest request, HttpStatus status, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setProperty("path", request.getRequestURI());

        String requestId = request.getHeader(REQ_ID_HEADER_NAME);
        if (requestId != null && !requestId.isBlank()) {
            problemDetail.setProperty("requestId", requestId);
        }
        return problemDetail;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) { super(message); }
        public ResourceNotFoundException() { super(); }
    }
}
