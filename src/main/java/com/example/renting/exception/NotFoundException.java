package com.example.renting.exception;

public class NotFoundException extends RentalException {

    private NotFoundException(String message) {
        super(message, 404);
    }

    public static NotFoundException ex(String message) {
        return new NotFoundException(message);
    }
}
