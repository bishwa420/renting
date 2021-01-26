package com.example.renting.apartment.model;

import javax.swing.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class AddApartmentRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be between 1 to 100 characters")
    public String name;

    @NotBlank(message = "Description is required")
    public String description;

    @NotBlank(message = "Latitude is required")
    @Size(max = 20, message = "Latitude can be at most 20 characters")
    public String latitude;

    @NotBlank(message = "longitude is required")
    @Size(max = 20, message = "Longitude can be at most 20 characters")
    public String longitude;

    @NotNull(message = "Price must be given")
    @Min(value = 1, message = "Price must be at least 1")
    public BigDecimal price;

    @NotNull(message = "Floor area must be given")
    @Min(value = 100, message = "At least 100 square feet apartments are allowed")
    public BigDecimal floorArea;

    @NotNull(message = "Number of rooms must be given")
    @Min(value = 1, message = "At least 1 room should be there")
    public int roomCount;

    @Override
    public String toString() {
        return "AddApartmentRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", price=" + price +
                ", floorArea=" + floorArea +
                ", roomCount=" + roomCount +
                '}';
    }
}
