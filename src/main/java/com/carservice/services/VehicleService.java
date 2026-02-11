package com.carservice.services;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import com.carservice.data.repositories.CarServiceRepository;
import com.carservice.data.repositories.RepairmentTypeRepository;
import com.carservice.data.repositories.VehicleRepository;
import com.carservice.web.model.MyVehiclesModel;
import com.carservice.web.model.VehicleModel;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final RepairmentTypeRepository repairmentTypeRepository;
    private final CarServiceRepository carServiceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository,
                          RepairmentTypeRepository repairmentTypeRepository,
                          CarServiceRepository carServiceRepository, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.repairmentTypeRepository = repairmentTypeRepository;
        this.carServiceRepository = carServiceRepository;
        this.modelMapper = modelMapper;
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.saveAndFlush(modelMapper.map(vehicle, Vehicle.class));
    }

    public Set<Vehicle> getAllVehiclesByOwner(User owner) {
        return vehicleRepository.getAllByOwner(owner);
    }
    
    public MyVehiclesModel buildMyVehiclesModel(VehicleModel vehicle) {
        MyVehiclesModel myVehiclesModel = modelMapper.map(vehicle, MyVehiclesModel.class);
        
        myVehiclesModel.setSelectedCarService(carServiceRepository
                .getCarServiceByName(vehicle.getSelectedCarService()));
        myVehiclesModel.setSelectedRepairmentType(repairmentTypeRepository
                .findRepairmentTypeByTypeOfRepairment(vehicle.getSelectedRepairmentType()));

        return myVehiclesModel;
    }

    public Vehicle getVehicleById(Long vehicleId) {
        return this.vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
    }
}
