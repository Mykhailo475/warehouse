package ua.wms.warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ua.wms.warehouse.dto.RegistrationForm;
import ua.wms.warehouse.service.UserService;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signUpPage(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String userRegistration(RegistrationForm registrationForm) {
        if(userService.isUserExist(registrationForm))
            return "redirect:/sign-up?user_exist";

        userService.registerClient(registrationForm);

        return "redirect:/login";
    }
}
