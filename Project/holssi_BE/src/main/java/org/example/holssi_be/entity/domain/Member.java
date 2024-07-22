package org.example.holssi_be.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String role;    // "USER", "COLLECTOR", "ADMIN"

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private Users user;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collectors collector;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore
    private Admins admin;

}
