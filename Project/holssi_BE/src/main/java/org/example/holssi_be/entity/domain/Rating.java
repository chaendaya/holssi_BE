package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating; // 평점 (1-5)

    @OneToOne
    @JoinColumn(name = "garbage_id")
    private Garbage garbage;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collectors collector;

}
