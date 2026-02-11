package com.carservice.web.controllers;

import com.carservice.data.entities.CarService;
import com.carservice.data.entities.User;
import com.carservice.data.repositories.CarServiceRepository;
import com.carservice.services.UserService;
import com.carservice.web.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class IndexController {
    private final UserService userService;
    private final CarServiceRepository carServiceRepository;
    private List<CarService> carServices = new ArrayList<>();

    @GetMapping("/unauthorized")
    public String unauthorized(HttpServletRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        request.getSession().setAttribute("user", user);

        return "/unauthorized";
    }

    @GetMapping("/")
    public String getIndex(HttpServletRequest request, Model model, Authentication authentication) {
        if (authentication != null) {
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("EMPLOYEE")
                            || auth.getAuthority().equals("ADMIN"))) {
                return "redirect:/employee-dashboard";
            }
        User user = (User) authentication.getPrincipal();
        request.getSession().setAttribute("user", user);

        }
        return "index";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        request.getSession().setAttribute("user", user);
        
        // Role-based redirect after successful login
        if (user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"))) {
            return "redirect:/repairments/assign";
        }
        if (user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("EMPLOYEE"))) {
            return "redirect:/employee-dashboard";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterForm(Authentication authentication) {
        if (authentication != null) {
            ModelAndView mav = new ModelAndView("/register");
            mav.addAllObjects(Map.of("carServices", getCarServices(), "userDto", new UserDto()));
            return mav;
        }
        return new ModelAndView("/register", "userDto", new UserDto());
    }

    @PostMapping("/register")
    public ModelAndView submitRegister(@Valid UserDto dto, BindingResult result) {
        if (result.hasErrors()) {
            //that means validation didn't pass
            ModelAndView mav = new ModelAndView("/register");

            mav.addAllObjects(result.getModel());
            mav.addObject("carServices", getCarServices());
            return mav;
        }

        try {
            userService.createUser(dto);

            return new ModelAndView("redirect:/login");

        } catch (Exception exception) {
            log.error(exception.getMessage());
            return new ModelAndView("/register");
        }

    }

    private List<CarService> getCarServices() {
        if (carServices.isEmpty() || carServices.size() != carServiceRepository.count()) {
            carServices = carServiceRepository.findAll();
        }
        return carServices;
    }

}
