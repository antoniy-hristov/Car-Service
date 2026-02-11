package com.carservice.data.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.time.Instant;

//TODO this entire class may need to be rewritten
//TODO this table should store all the repairments for the vehicles
@Entity
@Table(name = "repairment")
@Data
public class Repairment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "repairmentType_id", nullable = false)
    private RepairmentType repairmentType;

    @ManyToOne
    @JoinColumn(name = "carService_id", nullable = false)
    private CarService carService;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_employee_id")
    private User assignedEmployee;

    @CreatedDate
    private Instant creationDate;

    //this indicates whether a repairment has been completed
    private Boolean isCompleted;
}
