package com.carservice.web.dto;

import com.carservice.data.entities.CarService;
import com.carservice.data.entities.RepairmentType;
import com.carservice.data.entities.Vehicle;
import lombok.Data;

import java.time.Instant;

@Data
public class RepairmentDto {
    private Long id;
    private Vehicle vehicle;
    private RepairmentType repairmentType;
    private CarService carService;
    private Instant creationDate;
    //this indicates whether a repairment has been completed
    private Boolean isCompleted;
}
