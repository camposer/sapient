package com.sapient.com.sapient.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class RestControllerResponse {
    public interface Response extends Serializable {

    }

    public static class ErrorResponse implements Response {
        public String message;
        public String code;

        public ErrorResponse(Exception e) {
            this.message = e.getMessage();
            this.code = "0000"; // TODO add codes in the future
        }
    }

    public static ResponseEntity<Response> error(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<Response> success() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
