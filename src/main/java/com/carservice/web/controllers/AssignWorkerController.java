package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import com.carservice.services.UserService;
import com.carservice.services.VehicleService;
import com.carservice.web.model.AssignWorkerModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/assign-worker")
@RequiredArgsConstructor
public class AssignWorkerController {
    private final VehicleService vehicleService;
    private final UserService userService;

    @GetMapping
    public ModelAndView getAssignWorkerPage(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        model.addAttribute("vehicles", vehicleService.getAllVehiclesByOwner(user));
        model.addAttribute("users", userService.getAllEmployees());
        model.addAttribute("assignWorker", new AssignWorkerModel());

        return new ModelAndView("/assign-worker");
    }

    @PostMapping
    public ModelAndView assignWorker(@Valid @ModelAttribute AssignWorkerModel assignWorkerModel) {
        Optional<Vehicle>  vehicle = vehicleService.getVehicleById(assignWorkerModel.getVehicleId());
        Optional<User> user = userService.getUserById(assignWorkerModel.getEmployeeId());
        vehicle.get().setEmployee(user.get());

        vehicleService.saveVehicle(vehicle.get());
        return new ModelAndView("redirect:/my-vehicles");
    }
}
