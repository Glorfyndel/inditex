package com.example.inditex.domain;

public class PriceNotFoundException extends RuntimeException {
    private final String criteria;

    public PriceNotFoundException(String criteria) {
        super(message(criteria));
        this.criteria = criteria;
    }

    public String getCriteria() {
        return criteria;
    }

    private static String message(String criteria) {
        return "No price found for criteria: %s".formatted(criteria);
    }
}
