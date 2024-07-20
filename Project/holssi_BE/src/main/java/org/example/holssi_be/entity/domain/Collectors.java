package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "COLLECTORS")
public class Collectors {

    @Id
    @Column(name = "collector_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Member member;

    @Column(name = "location", nullable = false)
    private String location; // collection_area

    @OneToMany(mappedBy = "collector")
    private Set<Garbage> garbage;

    @OneToMany(mappedBy = "collector")
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "collector")
    private Set<Rating> ratings;

    public String getEmail() {
        return member.getEmail();
    }

}
