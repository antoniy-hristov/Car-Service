package com.carservice.services;

import com.carservice.data.entities.Repairment;
import com.carservice.data.repositories.RepairmentRepository;
import com.carservice.web.dto.RepairmentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairmentService {
    private final RepairmentRepository repairmentRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public RepairmentService(RepairmentRepository repairmentRepository, ModelMapper modelMapper) {
        this.repairmentRepository = repairmentRepository;
        this.modelMapper = modelMapper;
    }

    public List<Repairment> getRepairmentsByVehicleId(Long vehicleId) {
        return repairmentRepository.findByVehicle(vehicleId);
    }

    public Repairment saveRepairment(RepairmentDTO repairmentDTO) {
        return repairmentRepository.save(modelMapper.map(repairmentDTO, Repairment.class));
    }
}
