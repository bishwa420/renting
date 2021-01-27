package com.example.renting.apartment.db.repo;

import com.example.renting.apartment.db.entity.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    Page<Apartment> findByFloorAreaGreaterThanEqualAndFloorAreaLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualAndRoomCountGreaterThanEqualAndRoomCountLessThanEqualOrderByUpdatedAtDesc(long minFloorArea,
                                                                                                                                                                                                   long maxFloorArea,
                                                                                                                                                                                                   long minPrice,
                                                                                                                                                                                                   long maxPrice,
                                                                                                                                                                                                   long minRooms,
                                                                                                                                                                                                   long maxRooms,
                                                                                                                                                                                                   Pageable pageable);

    Page<Apartment> findByFloorAreaGreaterThanEqualAndFloorAreaLessThanEqualAndPriceGreaterThanEqualAndPriceLessThanEqualAndRoomCountGreaterThanEqualAndRoomCountLessThanEqualAndRealtorIdOrderByUpdatedAtDesc(long minFloorArea,
                                                                                                                                                                                                               long maxFloorArea,
                                                                                                                                                                                                               long minPrice,
                                                                                                                                                                                                               long maxPrice,
                                                                                                                                                                                                               long minRooms,
                                                                                                                                                                                                               long maxRooms,
                                                                                                                                                                                                               long realtorId,
                                                                                                                                                                                                               Pageable pageable);
}
