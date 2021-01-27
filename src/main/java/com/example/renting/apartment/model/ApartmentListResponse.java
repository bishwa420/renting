package com.example.renting.apartment.model;

import com.example.renting.apartment.db.entity.Apartment;
import com.example.renting.model.Page;

import java.util.List;
import java.util.stream.Collectors;

public class ApartmentListResponse {

    public static class ApartmentInfo {
        public Long apartmentId;
        public String name;
        public String description;
        public String longitude;
        public String latitude;
        public Long floorArea;
        public Long price;
        public Long roomCount;

        public ApartmentInfo(Apartment apartment) {

            this.apartmentId = apartment.id;
            this.name = apartment.name;
            this.description = apartment.description;
            this.longitude = apartment.longitude;
            this.latitude = apartment.latitude;
            this.floorArea = apartment.floorArea;
            this.price = apartment.price;
            this.roomCount = apartment.roomCount;
        }
    }

    public List<ApartmentInfo> apartmentList;
    public Page page;

    public static ApartmentListResponse of(org.springframework.data.domain.Page<Apartment> apartmentPage) {

        ApartmentListResponse response = new ApartmentListResponse();
        response.apartmentList = apartmentPage.getContent()
                .stream()
                .map(ApartmentInfo::new)
                .collect(Collectors.toList());
        response.page = Page.of(((Long)apartmentPage.getTotalElements()).intValue(),
                apartmentPage.getPageable().getPageNumber() + 1,
                apartmentPage.getPageable().getPageSize());
        return response;
    }
}
