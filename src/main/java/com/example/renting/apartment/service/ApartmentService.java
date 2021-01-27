package com.example.renting.apartment.service;

import com.example.renting.apartment.db.entity.Apartment;
import com.example.renting.apartment.db.repo.ApartmentRepository;
import com.example.renting.apartment.model.AddApartmentRequest;
import com.example.renting.apartment.model.ApartmentListResponse;
import com.example.renting.appuser.db.entity.User;
import com.example.renting.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ApartmentListResponse getApartments(Long minArea, Long maxArea, Long minPrice, Long maxPrice,
                                               Long minRooms, Long maxRooms, int page, int limit, UserInfo userInfo) {

        if(page < 1) page = 1;
        if(limit < 1 || limit > 100) limit = 100;

        Pageable pageable = PageRequest.of(page-1, limit);

        Page<Apartment> apartmentPage;

        if(userInfo.role.equals(User.Role.ADMIN.get())) {
            apartmentPage = apartmentRepository.findByFloorAreaGreaterThanEqualAndFloorAreaLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualAndRoomCountGreaterThanEqualAndRoomCountLessThanEqualOrderByUpdatedAtDesc(minArea,
                    maxArea, minPrice, maxPrice, minRooms, maxRooms, pageable);
        } else {
            apartmentPage = apartmentRepository.findByFloorAreaGreaterThanEqualAndFloorAreaLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualAndRoomCountGreaterThanEqualAndRoomCountLessThanEqualAndRealtorIdOrderByUpdatedAtDesc(minArea,
                    maxArea, minPrice, maxPrice, minRooms, maxRooms, userInfo.userId, pageable);
        }

        return ApartmentListResponse.of(apartmentPage);
    }
}
