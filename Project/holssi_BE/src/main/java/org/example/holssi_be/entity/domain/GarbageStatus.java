package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class GarbageStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "garbage_id")
    private Garbage garbage;

    private boolean matched;
    private boolean startCollection;
    private boolean collectionCompleted;

    @Temporal(TemporalType.TIMESTAMP)
    private Date collectionDate;

    @ManyToOne
    @JoinColumn(name = "collector_id", nullable = true) // null 허용
    private Collectors collector; // 새로운 필드 추가

    @PrePersist
    protected void onCreate() {
        matched = false;
        startCollection = false;
        collectionCompleted = false;
    }
}
