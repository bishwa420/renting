package com.example.renting.apartment.controller;

import com.example.renting.annotation.AdminPrivileged;
import com.example.renting.annotation.RealtorPrivileged;
import com.example.renting.apartment.model.AddApartmentRequest;
import com.example.renting.apartment.model.ApartmentListResponse;
import com.example.renting.apartment.service.ApartmentService;
import com.example.renting.model.BasicRestResponse;
import com.example.renting.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("")
@RestController
@RequestMapping("/apartment")
public class ApartmentController {

    private static final Logger log = LoggerFactory.getLogger(ApartmentController.class);

    @Autowired
    private ApartmentService apartmentService;

    @AdminPrivileged
    @RealtorPrivileged
    @PostMapping("")
    public ResponseEntity addApartment(@Valid @RequestBody AddApartmentRequest request,
                                       UserInfo userInfo) {

        log.info("Received ADD_APARTMENT request from user: {}", userInfo);

        apartmentService.addApartment(request, userInfo);

        return ResponseEntity.ok(BasicRestResponse.message("Apartment added successfully"));
    }

    @AdminPrivileged
    @RealtorPrivileged
    @GetMapping("")
    public ResponseEntity getApartments(@RequestParam(name = "minArea", defaultValue = "1") Long minArea,
                                        @RequestParam(name = "maxArea", defaultValue = "10000000000000000") Long maxArea,
                                        @RequestParam(name = "minPrice", defaultValue = "1") Long minPrice,
                                        @RequestParam(name = "maxPrice", defaultValue = "1000000000000000") Long maxPrice,
                                        @RequestParam(name = "minRooms", defaultValue = "1") Long minRooms,
                                        @RequestParam(name = "maxRooms", defaultValue = "1000") Long maxRooms,
                                        @RequestParam(name = "page", defaultValue = "1") int page,
                                        @RequestParam(name = "limit", defaultValue = "10") int limit,
                                        UserInfo userInfo) {

        ApartmentListResponse response = apartmentService.getApartments(minArea, maxArea, minPrice, maxPrice,
                minRooms, maxRooms, page, limit, userInfo);
        return ResponseEntity.ok(response);
    }
}
