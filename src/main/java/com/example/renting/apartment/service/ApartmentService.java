package com.example.renting.apartment.service;

import com.example.renting.apartment.db.entity.Apartment;
import com.example.renting.apartment.db.repo.ApartmentRepository;
import com.example.renting.apartment.model.AddApartmentRequest;
import com.example.renting.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApartmentService {

    private static final Logger log = LoggerFactory.getLogger(ApartmentService.class);

    @Autowired
    private ApartmentRepository apartmentRepository;

    public void addApartment(AddApartmentRequest request, UserInfo userInfo) {

        Apartment apartment = Apartment.of(request, userInfo);
        apartmentRepository.save(apartment);

        log.info("Stored the new apartment into DB successfully");
    }
}
