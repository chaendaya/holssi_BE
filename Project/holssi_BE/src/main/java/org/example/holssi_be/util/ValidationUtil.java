package org.example.holssi_be.util;

import org.example.holssi_be.exception.IllegalException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

@Component
public class ValidationUtil {

    // 유효성 검사 로직
    public static void validateRequest(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalException("Validation failed: " + errorMessage);
        }
    }
}
