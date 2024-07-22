package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Admins {

    @Id
    @Column(name = "admin_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Member member;
}
