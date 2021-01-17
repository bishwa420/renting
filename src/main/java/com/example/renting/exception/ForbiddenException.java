package com.example.renting.exception;

public class ForbiddenException extends RentalException {

    private ForbiddenException(String message) {
        super(message, 403);
    }

    public static ForbiddenException ex(String message) {
        return new ForbiddenException(message);
    }
}
