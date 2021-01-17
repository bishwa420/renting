package com.example.renting.exception;

public class RentalException extends RuntimeException {

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public RentalException() {
        super("Internal error!");
        this.message = "Internal error!";
        this.code = 500;
    }

    public RentalException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

    public RentalException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return "RentalException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
