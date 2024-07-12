package org.example.holssi_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private boolean success;
    private Object response;
    private String error;
}

