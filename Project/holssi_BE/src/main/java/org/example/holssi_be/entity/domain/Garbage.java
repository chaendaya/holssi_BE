package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    private String location;

    @ManyToOne
    @JoinColumn(name = "collector_id")
    private Collectors collector;

    @OneToOne(mappedBy = "garbage", cascade = CascadeType.ALL)
    private Rating rating;

    @OneToOne(mappedBy = "garbage", cascade = CascadeType.ALL)
    private CollectorLocation collectorLocation;


    @OneToOne(mappedBy = "garbage", cascade = CascadeType.ALL)
    private GarbageStatus status;

    // registrationDate 필드 자동으로 설정
    @PrePersist
    protected void onCreate() {
        registrationDate = new Date();
    }

    // daysSinceRegistration을 계산하는 메서드
    @Transient
    public long getDaysSinceRegistration() {
        long diffInMillies = Math.abs(new Date().getTime() - registrationDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}

