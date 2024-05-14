package ua.wms.warehouse.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.wms.warehouse.dto.EmployeeRegistrationForm;
import ua.wms.warehouse.security.UserPrincipal;
import ua.wms.warehouse.service.EmployeeService;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    @Value("${domen}")
    private String domen;

    private final EmployeeService employeeService;

    @GetMapping
    public String getEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        model.addAttribute("employees", employeeService.findMyEmployee(userPrincipal.getClient().getCompany()));
        model.addAttribute("employeeRegistrationForm", new EmployeeRegistrationForm());

        return "employees";
    }

    @PostMapping("/create")
    public String createEmployee(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid EmployeeRegistrationForm employeeRegistrationForm,
            BindingResult bindingResult
    ) {
        try {
            if (!bindingResult.hasErrors())
                 employeeService.createEmployee(employeeRegistrationForm, userPrincipal.getClient().getCompany());
        } catch (Exception e) {
            return "redirect:/employees?alreadyExists";
        }

        return "redirect:/employees";
    }



}
