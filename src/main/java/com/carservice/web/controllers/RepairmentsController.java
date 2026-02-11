package com.carservice.web.controllers;

import com.carservice.data.entities.Repairment;
import com.carservice.data.entities.User;
import com.carservice.services.RepairmentService;
import com.carservice.services.UserService;
import com.carservice.web.dto.RepairmentDto;
import com.carservice.web.dto.RepairmentForm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/repairments")
@RequiredArgsConstructor
public class RepairmentsController {
    private final UserService userService;
    private final RepairmentService repairmentService;

    /**
     * Employee page to view and manage assigned repairments
     */
    @GetMapping
    public String showRepairments(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        // Get repairments assigned to this employee's car service
        if (user.getCarService() != null) {
            List<RepairmentDto> repairments =
                    repairmentService.getRepairmentsByEmployee(user);
            RepairmentForm form = new RepairmentForm();
            form.setRepairments(repairments);

            model.addAttribute("repairmentForm", form);
        } else {
            model.addAttribute("repairmentForm", List.of());
        }

        return "repairments";
    }

    /**
     * Employee can mark assigned repairments as completed
     */
    @PostMapping
    public String updateRepairments(@ModelAttribute RepairmentForm repairmentForm) {

        repairmentForm.getRepairments()
                .stream()
                .filter(RepairmentDto::getIsCompleted)
                .forEach(r -> repairmentService.completeRepairment(r.getId()));

        return "redirect:/repairments";
    }

    /**
     * Admin page to assign pending repairments to employees
     */
    @GetMapping("/assign")
    @Transactional
    public ModelAndView getRepairmentAssignmentPage(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        // Get all pending repairments
        model.addAttribute("pendingRepairments", repairmentService.getPendingRepairments());

        // Get all employees
        model.addAttribute("employees", userService.getAllEmployees());

        ModelAndView mav = new ModelAndView("/repairments-assign");
        mav.addAllObjects(model.asMap());
        return mav;
    }

    /**
     * Assign a pending repairment to an employee as an Admin
     */
    @PostMapping("/assign")
    @Transactional
    public ModelAndView assignRepairmentToEmployee(@RequestParam Long repairmentId, @RequestParam Long employeeId) {
        Optional<User> employee = userService.getUserById(employeeId);
        employee.ifPresent(user -> repairmentService.assignRepairmentToEmployee(repairmentId, user));

        return new ModelAndView("redirect:/repairments/assign");
    }
}
