package com.carservice.data.repositories;

import com.carservice.data.entities.CarService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CarServiceRepository extends JpaRepository<CarService, Long> {

    Set<CarService> getCarServiceByDedicatedBrand(String dedicatedBrand);

    @Override
    List<CarService> findAll();

    CarService getCarServiceByName(String selectedCarService);
}
