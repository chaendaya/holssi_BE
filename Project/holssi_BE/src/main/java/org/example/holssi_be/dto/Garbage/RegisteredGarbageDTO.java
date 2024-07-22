package org.example.holssi_be.dto.Garbage;

import lombok.Data;

import java.util.Date;

@Data
public class RegisteredGarbageDTO {
    private Long garbageId;
    private boolean matched;
    private boolean startCollection;
    private String collectorName;
    private Date collectionDate;
}
