package org.example.holssi_be.dto.Garbage;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
public class AcceptGarbageDTO {

    @FutureOrPresent
    @NotBlank(message = "Collection date is required")
    private Date collectionDate;
}