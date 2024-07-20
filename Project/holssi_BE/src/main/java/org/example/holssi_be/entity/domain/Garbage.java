package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Garbage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "garbage_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private Double organicWeight;
    private Double inorganicWeight;
    private Double totalValue;
    private String status;
    private Date collectionDate;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collectors collector;

}
