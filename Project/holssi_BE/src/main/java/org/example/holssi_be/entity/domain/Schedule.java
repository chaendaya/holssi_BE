package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "garbage_id")
    private Garbage garbage;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collectors collector;

    private Date scheduledDate;
    private String pickupStatus;
}
