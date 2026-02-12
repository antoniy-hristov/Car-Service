package com.carservice.services;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import com.carservice.data.repositories.VehicleRepository;
import com.carservice.web.dto.VehicleDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService extends BaseService {

    private final VehicleRepository vehicleRepository;

    public Vehicle saveVehicle(VehicleDto dto) {
        Vehicle vehicle = map(dto, Vehicle.class);

        return vehicleRepository.saveAndFlush(map(vehicle, Vehicle.class));
    }

    @Transactional(readOnly = true)
    public Set<Vehicle> getAllVehiclesByOwner(User owner) {
        return vehicleRepository.getAllByOwner(owner);
    }
    
    public Vehicle getVehicleById(Long vehicleId) {
        return this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
    }
}
