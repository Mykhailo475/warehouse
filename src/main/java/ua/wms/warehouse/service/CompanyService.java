package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.dto.CompanyCreateForm;
import ua.wms.warehouse.entity.Client;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.User;
import ua.wms.warehouse.repository.CompanyRepository;
import ua.wms.warehouse.repository.UserRepository;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final UserRepository<User, Long> userRepository;

    public Company createCompany(CompanyCreateForm companyCreateForm, Client client){
        var company = new Company();
        company.setName(companyCreateForm.getName());
        company.setAddress(companyCreateForm.getAddress());

        if(company.getUsers() == null){
            company.setUsers(new HashSet<>());
        }
        company.getUsers().add(client);
        client.setCompany(company);

        companyRepository.save(company);
        userRepository.save(client);

        return company;
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }
}
