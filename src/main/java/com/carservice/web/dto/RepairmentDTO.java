package com.carservice.web.dto;

import com.carservice.data.entities.CarService;
import com.carservice.data.entities.RepairmentType;
import com.carservice.data.entities.Vehicle;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class RepairmentDTO {
    private Long id;

    private Vehicle vehicle;

    private RepairmentType repairmentType;

    private CarService carService;

    private Timestamp creationDate;

    //this indicates whether a repairment has been completed
    private Boolean isCompleted;
}
