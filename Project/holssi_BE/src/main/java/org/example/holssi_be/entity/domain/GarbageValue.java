package org.example.holssi_be.entity.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.example.holssi_be.exception.IllegalException;

@Entity
@Data
public class GarbageValue {

    @Id
    private long id = 1L;   // Always set to 1

    @Column(nullable = false)
    private double organic;

    @Column(nullable = false)
    private double non_organic;

    @PrePersist
    @PreUpdate
    protected void prePersistAndUpdate() {
        if (this.id != 1L) {
            throw new IllegalException("Only one row with id=1 is allowed.");
        }
        // 필드의 초기 값 설정
        if (organic == 0 && non_organic == 0) {
            this.organic = 60;
            this.non_organic = 80;
        }
    }
}
