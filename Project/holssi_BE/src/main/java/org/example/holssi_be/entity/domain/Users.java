package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "USERS")
public class Users {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Id
    @Column(name = "user_email", nullable = false, length = 255)
    private String userEmail;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone", nullable = false, length = 255)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "account", nullable = false, length = 255)
    private String account;

    @Column(name = "bank", nullable = false, length = 255)
    private String bank;

}
