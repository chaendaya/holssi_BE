package org.example.holssi_be.exception;

import org.example.holssi_be.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidMemException.class)
    public ResponseEntity<ResponseDTO> handleInvalidMemException(InvalidMemException ex) {
        ResponseDTO response = new ResponseDTO(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ResponseDTO> handleVerificationException(VerificationException ex) {
        ResponseDTO response = new ResponseDTO(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ResponseDTO> handleInvalidRoleException(InvalidRoleException ex) {
        ResponseDTO response = new ResponseDTO(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleMemberNotFoundException(MemberNotFoundException ex) {
        return new ResponseEntity<>(new ResponseDTO(false, null, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenFormatException.class)
    public ResponseEntity<ResponseDTO> handleInvalidTokenFormatException(InvalidTokenFormatException ex) {
        return new ResponseEntity<>(new ResponseDTO(false, null, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseDTO response = new ResponseDTO(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ResponseDTO> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        ResponseDTO response = new ResponseDTO(false, null, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception ex) {
        ResponseDTO response = new ResponseDTO(false, null, "Internal Server Error");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
