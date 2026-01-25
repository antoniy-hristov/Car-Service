package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.entities.Vehicle;
import com.carservice.data.repositories.CarServiceRepository;
import com.carservice.data.repositories.RepairmentTypeRepository;
import com.carservice.web.dto.RepairmentDTO;
import com.carservice.services.RepairmentService;
import com.carservice.services.VehicleService;
import com.carservice.web.model.VehicleModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/schedule-repairment")
public class ScheduleRepairmentController {

    private final RepairmentTypeRepository repairmentTypeRepository;
    private final VehicleModel transitionalModel = new VehicleModel();
    private final VehicleService vehicleService;
    private final CarServiceRepository carServiceRepository;
    private final RepairmentService repairmentService;
    private final ModelMapper modelMapper;


    @Autowired
    public ScheduleRepairmentController(RepairmentTypeRepository repairmentTypeRepository,
                                        VehicleService vehicleService, CarServiceRepository carServiceRepository,
                                        RepairmentService repairmentService, ModelMapper modelMapper) {
        this.repairmentTypeRepository = repairmentTypeRepository;
        this.vehicleService = vehicleService;
        this.carServiceRepository = carServiceRepository;
        this.repairmentService = repairmentService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("vehicle")
    private Model getModel(Model model) {
        this.transitionalModel.setRepairmentTypes(repairmentTypeRepository.findAll());

        model.addAttribute("vehicle", transitionalModel);
        model.addAttribute("carServicesList", carServiceRepository.findAll());

        return model;
    }

    @GetMapping
    public ModelAndView getScheduleRepairment() {
        VehicleModel vehicle = new VehicleModel();
        vehicle.setRepairmentTypes(repairmentTypeRepository.findAll());

        return new ModelAndView("/schedule-repairment", "vehicle", vehicle);
    }

    @PostMapping
    public ModelAndView scheduleRepairment(HttpServletRequest request,
                                           @Valid @ModelAttribute("vehicle") VehicleModel vehicle, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("/schedule-repairment", "vehicle", vehicle);
        }
        User user = (User) request.getSession().getAttribute("user");
        vehicle.setOwner(user);

        Vehicle savedVehicle = vehicleService.saveVehicle(modelMapper.map(vehicle, Vehicle.class));
        /*TODO a repairment ticket has to be created once a vehicle is registered
        *  that means that someone needs to be assigned to that vehicle and the repairment may start
        *  however, there is an issue with setting the necessary fields because for some reason
        *  thymeleaf works correctly only with getting the strings from the form, nothing else
        *  the vehicle class needs to be reworked and a single vehicle shouldn't take more than one  repairment
        * */
        RepairmentDTO repairment = new RepairmentDTO();
        repairment.setRepairmentType(vehicle.getRepairmentTypes().get(0));
        repairment.setVehicle(savedVehicle);
        repairment.setCarService(carServiceRepository.
                getCarServiceByName(vehicle.getSelectedCarService()));
        repairment.setIsCompleted(false);
        repairment.setCreationDate(Timestamp.valueOf(LocalDateTime.now()));
        repairmentService.saveRepairment(repairment);
//        return new ModelAndView("redirect:/my-vehicles", "vehicles",
//                vehicleService.buildMyVehiclesModel(vehicle));
        return new ModelAndView("redirect:/my-vehicles");
    }

}
