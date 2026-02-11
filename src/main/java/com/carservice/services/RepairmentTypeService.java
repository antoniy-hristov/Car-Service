package com.carservice.services;

import com.carservice.data.entities.RepairmentType;
import com.carservice.data.repositories.RepairmentRepository;
import com.carservice.data.repositories.RepairmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepairmentTypeService extends BaseService {
    private final RepairmentTypeRepository repairmentTypeRepository;
    private final ModelMapper modelMapper;

    public List<RepairmentType> getAllRepairmentTypes() {
        return repairmentTypeRepository.findAll();
    }

}
