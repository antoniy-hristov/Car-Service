package com.carservice.web.controllers;

import com.carservice.data.entities.CarService;
import com.carservice.data.repositories.CarServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/car-services")
public class CarServiceController {

    private final CarServiceRepository carServiceRepository;

    @Autowired
    public CarServiceController(CarServiceRepository carServiceRepository) {
        this.carServiceRepository = carServiceRepository;
    }

    @GetMapping
    public String getCarServices(Model model) {
        List<CarService> carServices = carServiceRepository.findAll();

        model.addAttribute("carServices", carServices);
        return "/car-services";
    }
}
