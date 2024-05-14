package ua.wms.warehouse.controller.company;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.wms.warehouse.dto.CompanyCreateForm;
import ua.wms.warehouse.security.UserPrincipal;
import ua.wms.warehouse.service.CompanyService;

@Controller
@RequestMapping("/company")
public class CompanyController {


    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/manage")
    public String manage(Model model,
                         @AuthenticationPrincipal UserPrincipal userPrincipal){
        model.addAttribute("company",userPrincipal.getUser().getCompany());
        return "company/manage";
    }

    @GetMapping("/manage/create")
    public String create(Model model){
        model.addAttribute("companyCreateForm", new CompanyCreateForm());
        return "company/create";
    }
    @PostMapping("/manage/create")
    public String createCompany(CompanyCreateForm companyCreateForm,
                                @AuthenticationPrincipal UserPrincipal userPrincipal){
        companyService.createCompany(companyCreateForm,userPrincipal.getClient());
        return "redirect:/company/manage";
    }

}
