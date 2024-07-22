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
    private Double non_organicWeight;
    private Double totalWeight;
    private Double totalValue;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    private long daysSinceRegistration;
    private String location;

    private boolean matched;    // 수거인 매칭 여부
    private Date collectionDate;
    private boolean startCollection; // 수거 시작 여부

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collectors collector;

    // registrationDate 필드 자동으로 설정
    @PrePersist
    protected void onCreate() {

        registrationDate = new Date(); // 등록 시 현재 시간을 설정
        daysSinceRegistration = 0;
    }

}
