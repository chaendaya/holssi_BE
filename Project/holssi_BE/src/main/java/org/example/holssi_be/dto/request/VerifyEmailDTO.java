package org.example.holssi_be.dto.request;

import lombok.Data;

@Data
public class VerifyEmailDTO {
    private String email;
    private String code;
}
