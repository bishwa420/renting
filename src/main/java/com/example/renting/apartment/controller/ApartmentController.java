package com.example.renting.apartment.controller;

import com.example.renting.annotation.AdminPrivileged;
import com.example.renting.annotation.RealtorPrivileged;
import com.example.renting.apartment.model.AddApartmentRequest;
import com.example.renting.model.BasicRestResponse;
import com.example.renting.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("")
@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    private static final Logger log = LoggerFactory.getLogger(ApartmentController.class);

    @AdminPrivileged
    @RealtorPrivileged
    @PostMapping("")
    public ResponseEntity addApartment(@Valid @RequestBody AddApartmentRequest request,
                                       UserInfo userInfo) {

        log.info("Received ADD_APARTMENT request from user: {}", userInfo);
        return ResponseEntity.ok(BasicRestResponse.message("Apartment added successfully"));
    }
}
