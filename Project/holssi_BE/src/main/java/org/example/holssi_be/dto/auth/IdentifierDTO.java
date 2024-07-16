package org.example.holssi_be.dto.auth;

import lombok.Data;

@Data
public class IdentifierDTO {
    private String primaryKey;  // email
    private String identifier;
}
