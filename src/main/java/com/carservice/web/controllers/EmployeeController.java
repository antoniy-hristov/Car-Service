package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.repositories.RepairmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee-dashboard")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final RepairmentRepository repairmentRepository;

    @GetMapping
    public String getEmployeeDashboard(HttpServletRequest request, Model model, Authentication authentication) {
        User employee = (User) authentication.getPrincipal();
        request.getSession().setAttribute("user", employee);

        // Add employee information
        model.addAttribute("employee", employee);
        model.addAttribute("carService", employee.getCarService());

        // Get repairments assigned to this employee's car service
        if (employee.getCarService() != null) {
            model.addAttribute("repairments",
                    repairmentRepository.findAll().stream()
                            .filter(r -> r.getCarService().equals(employee.getCarService()))
                            .toList());
        } else {
            model.addAttribute("repairments", java.util.Collections.emptyList());
        }

        return "/employee-dashboard";
    }
}
