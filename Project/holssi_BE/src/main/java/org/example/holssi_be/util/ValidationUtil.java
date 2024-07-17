package org.example.holssi_be.util;

import org.example.holssi_be.exception.InvalidUserException;
import org.springframework.validation.BindingResult;

public class ValidationUtil {
    // 유효성 검사 로직

    public static void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidUserException(bindingResult.getFieldError().getDefaultMessage());
        }
    }
}
