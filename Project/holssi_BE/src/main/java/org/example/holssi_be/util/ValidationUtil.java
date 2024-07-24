package org.example.holssi_be.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class ValidationUtil {

    // 유효성 검사 로직
    public static void validateRequest(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Validation failed: " + errorMessage);
        }
    }
}
