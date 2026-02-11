package com.carservice.web.dto;

import com.carservice.data.entities.RepairmentType;
import com.carservice.data.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Data
public class VehicleDto implements Serializable {
    private Long vehicle_id;
    @Pattern(regexp = "[A-Z]{1,2}[0-9]*[A-Z]{2}",
            message = "Your license plate input doesn't match the standard!")
    private String licensePlateNumber;
    @NotEmpty(message = "Vehicle Brand cannot be empty!")
    private String vehicleBrand;
    @NotEmpty(message = "Vehicle Model cannot be empty!")
    private String vehicleModel;
    private Year yearOfManufacture;
    private User owner;
    private List<RepairmentType> repairmentTypes;
    @NotEmpty(message = "A car service must be selected!")
    private String selectedCarService;
    @NotEmpty(message = "A repairment service must be selected!")
    private String selectedRepairmentType;
    private LocalDateTime selectedTimeForRepair;
    private User employee;
    private Long selectedVehicleId;
    @Override
    public String toString() {
        return "VehicleModel{" +
                "vehicle_id=" + vehicle_id +
                ", licensePlateNumber='" + licensePlateNumber + '\'' +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", yearOfManufacture=" + yearOfManufacture +
                ", owner=" + owner +
                ", repairmentTypes=" + repairmentTypes +
                ", selectedCarService='" + selectedCarService + '\'' +
                ", selectedRepairmentType='" + selectedRepairmentType + '\'' +
                ", selectedTimeForRepair=" + selectedTimeForRepair +
                '}';
    }
}
