package org.example.holssi_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO {
    private boolean success;
    private Object response;
    private String error;

    // 성공 응답 생성자
    public ResponseDTO(boolean success, Object response) {
        this.success = success;
        this.response = response;
        this.error = null;
    }

    // 오류 응답 생성자
    public ResponseDTO(boolean success, String error) {
        this.success = success;
        this.response = null;
        this.error = error;
    }
}

