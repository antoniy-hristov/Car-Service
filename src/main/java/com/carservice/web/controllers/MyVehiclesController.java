package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import com.carservice.services.RepairmentService;
import com.carservice.services.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/my-vehicles")
@RequiredArgsConstructor
public class MyVehiclesController {
    private final VehicleService vehicleService;
    private final RepairmentService repairmentService;

    @GetMapping
    @Transactional
    public String getMyVehicles(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        // Get all vehicles for this user
        Set<Vehicle> vehicles = vehicleService.getAllVehiclesByOwner(user);
        
        // Force load repairments for each vehicle within transaction context
        vehicles.forEach(vehicle -> vehicle.getRepairments().size());
        
        model.addAttribute("vehicles", vehicles);

        return "/my-vehicles";
    }
}
