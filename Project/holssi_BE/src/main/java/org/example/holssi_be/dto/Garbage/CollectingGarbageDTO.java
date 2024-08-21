package org.example.holssi_be.dto.Garbage;

import lombok.Data;

@Data
public class CollectingGarbageDTO {
    private Long garbageId;
    private boolean started;
}
