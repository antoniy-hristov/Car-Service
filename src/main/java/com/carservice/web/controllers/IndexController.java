package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.repositories.RoleMapper;
import com.carservice.services.UserService;
import com.carservice.services.VehicleService;
import com.carservice.web.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class IndexController {
    private final RoleMapper roleMapper;
    private final VehicleService vehicleService;
    private final UserService userService;

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
    public ModelAndView getRegisterForm() {
        return new ModelAndView("/register", "userDto", new UserDto());
    }

    @PostMapping("/register")
    public String submitRegister(@Valid UserDto dto,
                                 BindingResult result,
                                 @RequestParam(value = "isEmployee", required = false) boolean isEmployee) {
        if (result.hasErrors()) {
            //that means validation didn't pass
            return "/register";
        }
        try {
            if (isEmployee) {
                dto.setRole_id(roleMapper.findRoleByAuthority("EMPLOYEE"));
            } else {
                dto.setRole_id(roleMapper.findRoleByAuthority("CUSTOMER"));
            }
            /**
             *All users are created as CUSTOMER by default, in order for a user to be ADMIN
             *he has to be manually set as one
             **/
            //TODO figure out the worker logic - would there be a different logic for registering a worker
            // or would it be set manually as well
            userService.createUser(dto);

            return "redirect:/login";

        } catch (Exception exception) {
            log.error(exception.getMessage());
            return "/register";

        }

    }

}
