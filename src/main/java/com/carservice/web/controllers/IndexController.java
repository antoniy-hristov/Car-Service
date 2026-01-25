package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import com.carservice.data.repositories.RoleMapper;
import com.carservice.data.repositories.UserRepository;
import com.carservice.services.VehicleService;
import com.carservice.web.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@ControllerAdvice
@Slf4j
public class IndexController {
    private final RoleMapper roleMapper;
    private UserModel transitionalModel = new UserModel();
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final VehicleService vehicleService;

    @Autowired
    public IndexController(RoleMapper roleMapper, BCryptPasswordEncoder bCryptPasswordEncoder,
                           UserRepository userRepository, ModelMapper modelMapper, VehicleService vehicleService) {
        this.roleMapper = roleMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/unauthorized")
    public String unauthorized(HttpServletRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        request.getSession().setAttribute("user", user);

        return "/unauthorized";
    }

    @ModelAttribute("user")
    private void getModel(Model model) {
        model.addAttribute("user", transitionalModel);
    }

    @GetMapping("/")
    public String getIndex(HttpServletRequest request, Model model, Authentication authentication) {
        final String welcomeMessage = "Welcome to the Car Service System!";
        model.addAttribute("welcome", welcomeMessage);

        User user = (User) authentication.getPrincipal();
        request.getSession().setAttribute("user", user);
        return "index";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "/login";
    }

    @PostMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterForm() {
        UserModel user = new UserModel();

        transitionalModel = user;

        user.setIsEmployee(Boolean.TRUE);
        return new ModelAndView("/register", "user", user);
    }

    @PostMapping("/register")
    public String submitRegister(@Valid @ModelAttribute("user") UserModel user,
                                 BindingResult result) {
        if (result.hasErrors()) {
            //that means validation didn't pass
            return "/register";
        }
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));
            /**
             *All users are created as CUSTOMER by default, in order for a user to be ADMIN
             *he has to be manually set as one
             **/
            //TODO figure out the worker logic - would there be a different logic for registering a worker
            // or would it be set manually as well
            if (user.getIsEmployee()) {
                user.setRole_id(roleMapper.findRoleByAuthority("EMPLOYEE"));
            } else {
            user.setRole_id(roleMapper.findRoleByAuthority("CUSTOMER"));
            }

            userRepository.saveAndFlush(modelMapper.map(user, User.class));
            return "redirect:/login";

        } catch (Exception exception) {
            log.error(exception.getMessage());
            return "/register";

        }

    }

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

}
