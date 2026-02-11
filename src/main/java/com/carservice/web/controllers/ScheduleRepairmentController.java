package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import com.carservice.data.repositories.CarServiceRepository;
import com.carservice.services.RepairmentTypeService;
import com.carservice.web.dto.RepairmentDto;
import com.carservice.services.RepairmentService;
import com.carservice.services.VehicleService;
import com.carservice.web.dto.VehicleDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/schedule-repairment")
@RequiredArgsConstructor
public class ScheduleRepairmentController {
    private final RepairmentTypeService repairmentTypeService;
    private final VehicleDto transitionalModel = new VehicleDto();
    private final VehicleService vehicleService;
    private final CarServiceRepository carServiceRepository;
    private final RepairmentService repairmentService;
    private final ModelMapper modelMapper;

    @ModelAttribute("vehicle")
    private Model getModel(Model model) {
        this.transitionalModel.setRepairmentTypes(repairmentTypeService.getAllRepairmentTypes());

        model.addAttribute("vehicle", transitionalModel);
        model.addAttribute("carServicesList", carServiceRepository.findAll());

        return model;
    }

    @GetMapping
    public ModelAndView getScheduleRepairment(HttpServletRequest request) {
        VehicleDto vehicle = new VehicleDto();
        vehicle.setRepairmentTypes(repairmentTypeService.getAllRepairmentTypes());
        
        User user = (User) request.getSession().getAttribute("user");
        Set<Vehicle> userVehicles = user != null ? user.getVehicles() : null;
        
        ModelAndView modelAndView = new ModelAndView("/schedule-repairment", "vehicle", vehicle);
        modelAndView.addObject("userVehicles", userVehicles);
        modelAndView.addObject("carServicesList", carServiceRepository.findAll());

        return modelAndView;
    }

    @PostMapping
    public ModelAndView scheduleRepairment(HttpServletRequest request,
                                           @Valid @ModelAttribute("vehicle") VehicleDto vehicle, BindingResult result) {
        if (result.hasErrors()) {
            User user = (User) request.getSession().getAttribute("user");
            Set<Vehicle> userVehicles = user != null ? user.getVehicles() : null;
            ModelAndView modelAndView = new ModelAndView("/schedule-repairment", "vehicle", vehicle);
            modelAndView.addObject("userVehicles", userVehicles);
            modelAndView.addObject("carServicesList", carServiceRepository.findAll());
            return modelAndView;
        }
        User user = (User) request.getSession().getAttribute("user");
        vehicle.setOwner(user);

        // Check if a pre-existing vehicle was selected
        Vehicle savedVehicle;
        if (vehicle.getSelectedVehicleId() != null && vehicle.getSelectedVehicleId() > 0) {
            // Use the existing vehicle - don't save it again
            savedVehicle = vehicleService.getVehicleById(vehicle.getSelectedVehicleId());
        } else {
            // Save as a new vehicle
            savedVehicle = vehicleService.saveVehicle(vehicle);
        }
        
        /*TODO a repairment ticket has to be created once a vehicle is registered
        *  that means that someone needs to be assigned to that vehicle and the repairment may start
        *  however, there is an issue with setting the necessary fields because for some reason
        *  thymeleaf works correctly only with getting the strings from the form, nothing else
        *  the vehicle class needs to be reworked and a single vehicle shouldn't take more than one  repairment
        * */
        RepairmentDto repairment = new RepairmentDto();
        repairment.setRepairmentType(vehicle.getRepairmentTypes().get(0));
        repairment.setVehicle(savedVehicle);
        repairment.setCarService(carServiceRepository.
                getCarServiceByName(vehicle.getSelectedCarService()));
        repairment.setIsCompleted(false);
        repairment.setCreationDate(Instant.now());
        repairmentService.saveRepairment(repairment);

        return new ModelAndView("redirect:/my-vehicles");
    }

}
