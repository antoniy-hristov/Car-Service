package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.services.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my-vehicles")
@RequiredArgsConstructor
public class MyVehiclesController {
    private final VehicleService vehicleService;

    @GetMapping
    public String getMyVehicles(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        model.addAttribute("vehicles", vehicleService.getAllVehiclesByOwner(user));

        return "/my-vehicles";
    }
}
