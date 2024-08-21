package org.example.holssi_be.exception;

import org.example.holssi_be.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCodeException.class)
    public ResponseEntity<ResponseDTO> handleInvalidCodeException(InvalidCodeException ex) {
        ResponseDTO response = new ResponseDTO(false, "Invalid: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ResponseDTO> handleInvalidMemException(InvalidException ex) {
        ResponseDTO response = new ResponseDTO(false, "Invalid: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ResponseDTO> handleInvalidRoleException(InvalidRoleException ex) {
        ResponseDTO response = new ResponseDTO(false, "Invalid: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ResponseDTO> handleInvalidTokenException(InvalidTokenException ex) {
        ResponseDTO responseDTO = new ResponseDTO(false, "Invalid: " + ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleMemberNotFoundException(MemberNotFoundException ex) {
        ResponseDTO response = new ResponseDTO(false, "Not Found: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseDTO response = new ResponseDTO(false, "Not Found: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotUserException.class)
    public ResponseEntity<ResponseDTO> handleNotUserException(NotUserException ex) {
        ResponseDTO response = new ResponseDTO(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotCollectorException.class)
    public ResponseEntity<ResponseDTO> handleNotCollectorException(NotCollectorException ex) {
        ResponseDTO response = new ResponseDTO(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotAdminException.class)
    public ResponseEntity<ResponseDTO> handleNotAdminException(NotAdminException ex) {
        ResponseDTO response = new ResponseDTO(false, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalException ex) {
        ResponseDTO responseDTO = new ResponseDTO(false, ex.getMessage());
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception ex) {
        ResponseDTO response = new ResponseDTO(false, "Internal Server Error: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
