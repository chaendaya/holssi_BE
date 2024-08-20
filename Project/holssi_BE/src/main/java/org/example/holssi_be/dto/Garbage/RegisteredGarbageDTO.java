package org.example.holssi_be.dto.Garbage;

import lombok.Data;

import java.util.Date;

@Data
public class RegisteredGarbageDTO {
    private Long garbageId;
    private boolean matched;
    private double organicWeight;
    private double non_organicWeight;
    private double saving;
    private Date registrationDate;
}
