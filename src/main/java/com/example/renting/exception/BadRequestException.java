package com.example.renting.exception;

public class BadRequestException extends RentalException {

    public BadRequestException(String message) {
        super(message, 400);
    }

    public static BadRequestException ex(String message) {

        return new BadRequestException(message);
    }
}
