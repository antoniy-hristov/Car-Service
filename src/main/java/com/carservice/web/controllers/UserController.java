package com.carservice.web.controllers;

import com.carservice.data.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    private User transitionalModel = new User();

    @ModelAttribute("user")
    private void getModel(Model model) {
        model.addAttribute("user", transitionalModel);
    }

}
