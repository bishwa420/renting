package com.example.renting.model;

public class BasicRestResponse {

    public String message;

    public static BasicRestResponse ok(String message) {

        BasicRestResponse response = new BasicRestResponse();
        response.message = message;
        return response;
    }
}
