package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "phone",nullable = false)
    private String phone;

    @Column(name = "role",nullable = false)
    private String role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Users user;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Collectors collector;

}