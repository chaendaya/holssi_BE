package org.example.holssi_be.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String userEmail;
    private String password;
    private String phone;
    private String location;
    private String account;
    private String bank;
}
