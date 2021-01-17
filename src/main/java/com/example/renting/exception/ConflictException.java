package com.example.renting.exception;

public class ConflictException extends RentalException {

    private ConflictException(String message) {
        super(message, 409);
    }

    public static ConflictException ex(String message) {

        return new ConflictException(message);
    }
}
