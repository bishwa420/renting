package com.example.renting.apartment.db.entity;

import com.example.renting.apartment.model.AddApartmentRequest;
import com.example.renting.model.UserInfo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "apartment")
@Entity
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "room_count")
    public Long roomCount;

    @Column(name = "price")
    public Long price;

    @Column(name = "latitude")
    public String latitude;

    @Column(name = "longitude")
    public String longitude;

    @Column(name = "realtor_id")
    public Long realtorId;

    @Column(name = "floor_area")
    public Long floorArea;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    public static Apartment of(AddApartmentRequest request, UserInfo userInfo) {

        Apartment apartment = new Apartment();
        apartment.name = request.name;
        apartment.description = request.description;

        // TODO verify the latitude & longitude
        apartment.latitude = request.latitude;
        apartment.longitude = request.longitude;

        apartment.price = request.price;
        apartment.floorArea = request.floorArea;
        apartment.roomCount = request.roomCount;
        apartment.realtorId = userInfo.userId;
        apartment.updatedAt = LocalDateTime.now();

        return apartment;
    }

    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", roomCount=" + roomCount +
                ", price=" + price +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", realtorId=" + realtorId +
                '}';
    }
}
