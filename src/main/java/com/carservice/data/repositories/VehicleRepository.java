package com.carservice.data.repositories;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Set<Vehicle> getAllByOwner(User user);
}
