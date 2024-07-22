package org.example.holssi_be.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
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
    @JsonIgnore
    private Set<Garbage> garbage;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Schedule> schedules;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Rating> ratings;

    public String getEmail() {
        return member.getEmail();
    }
}
