package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "USERS")
public class Users {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Member member;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "account", nullable = false)
    private String account;

    @Column(name = "bank", nullable = false)
    private String bank;

    @OneToMany(mappedBy = "user")
    private Set<Garbage> garbage;

    @OneToMany(mappedBy = "user")
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "user")
    private Set<Rating> ratings;

    public String getEmail() {
        return member.getEmail();
    }
}
