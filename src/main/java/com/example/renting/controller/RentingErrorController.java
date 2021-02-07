package com.example.renting.controller;

import com.example.renting.model.BasicRestResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class RentingErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity handleError() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(BasicRestResponse.message("Requested resource not found"));
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
