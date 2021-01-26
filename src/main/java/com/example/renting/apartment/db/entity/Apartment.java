package com.example.renting.apartment.db.entity;

import javax.persistence.*;
import javax.print.attribute.standard.MediaSize;
import java.math.BigDecimal;

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
    public int roomCount;

    @Column(name = "price")
    public BigDecimal price;

    @Column(name = "latitude")
    public String latitude;

    @Column(name = "longitude")
    public String longitude;

    @Column(name = "realtor_id")
    public Long realtorId;

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
