package org.example.holssi_be.entity.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "COLLECTORS")
public class Collectors {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Id
    @Column(name = "collector_email", nullable = false, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "phone", nullable = false, length = 255)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

}
