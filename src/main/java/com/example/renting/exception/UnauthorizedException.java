package com.example.renting.exception;

public class UnauthorizedException extends RentalException {

    private UnauthorizedException(String message) {
        super(message, 401);
    }

    public static UnauthorizedException ex(String message) {
        return new UnauthorizedException(message);
    }
}
