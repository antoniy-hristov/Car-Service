package com.carservice.services;

import com.carservice.data.entities.CarService;
import com.carservice.data.entities.Repairment;
import com.carservice.data.entities.User;
import com.carservice.data.repositories.RepairmentRepository;
import com.carservice.web.dto.RepairmentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RepairmentService extends BaseService {
    private final RepairmentRepository repairmentRepository;

    public List<Repairment> getRepairmentsByVehicleId(Long vehicleId) {
        return repairmentRepository.findByVehicle(vehicleId);
    }

    public Repairment saveRepairment(RepairmentDto repairmentDTO) {
        return repairmentRepository.save(map(repairmentDTO, Repairment.class));
    }

    public List<Repairment> getPendingRepairments() {
        return repairmentRepository.findAll().stream()
                .filter(r -> r.getAssignedEmployee() == null && !r.getIsCompleted())
                .toList();
    }

    public List<Repairment> getRepairmentsByCarService(CarService carService) {
        return repairmentRepository.findByCarService(carService);
    }

    public List<RepairmentDto> getRepairmentsByEmployee(User employee) {
        return mapToList(repairmentRepository.findByAssignedEmployee(employee), RepairmentDto.class);
    }

    public Repairment assignRepairmentToEmployee(Long repairmentId, User employee) {
        Repairment repairment = repairmentRepository.findById(repairmentId).orElseThrow(
                () -> new IllegalArgumentException("Repairment not found with id: " + repairmentId));
        if (repairment != null) {
            repairment.setAssignedEmployee(employee);
            return repairmentRepository.save(repairment);
        }
        return null;
    }

    public Repairment completeRepairment(Long repairmentId) {
        Repairment repairment = repairmentRepository.findById(repairmentId).orElse(null);
        if (repairment != null) {
            repairment.setIsCompleted(true);
            return repairmentRepository.save(repairment);
        }
        return null;
    }
}
