package org.example.holssi_be.dto.Garbage;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class AcceptGarbageDTO {
    @NotNull
    @FutureOrPresent
    private Date collectionDate;
}