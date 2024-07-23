package org.example.holssi_be.util;

import org.example.holssi_be.exception.InvalidMemException;
import org.springframework.validation.BindingResult;

public class ValidationUtil {

    // 유효성 검사 로직
    public static void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidMemException(bindingResult.getFieldError().getDefaultMessage());
        }
    }
}
