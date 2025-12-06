package com.example.app.dbdesignbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {
    private String message;
    private List<Violation> violations;

    @Getter
    public static class Violation {
        private final String fieldName;
        private final String message;

        public Violation(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

    }
}
