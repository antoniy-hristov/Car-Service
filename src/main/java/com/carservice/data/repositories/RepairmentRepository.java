package com.carservice.data.repositories;

import com.carservice.data.entities.CarService;
import com.carservice.data.entities.Repairment;
import com.carservice.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairmentRepository extends JpaRepository<Repairment, Long> {
    List<Repairment> findByVehicle(Long vehicleId);
    List<Repairment> findByCarService(CarService carService);
    List<Repairment> findByAssignedEmployee(User employee);
}
