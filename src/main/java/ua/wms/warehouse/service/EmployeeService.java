package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.dto.EmployeeRegistrationForm;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Employee;
import ua.wms.warehouse.exceptions.UserExistException;
import ua.wms.warehouse.repository.EmployeeRepository;
import ua.wms.warehouse.security.Authority;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final CompanyService companyService;

    public List<Employee> findMyEmployee(Company company) {

        return employeeRepository.findAllByCompany(company);
    }

    public void createEmployee(EmployeeRegistrationForm registrationForm, Company company) throws UserExistException {

        if (employeeRepository.existsByTelephone(registrationForm.getTelephone()) || employeeRepository.existsByEmail(registrationForm.getEmail())) {
            log.error("User exist");
            throw new UserExistException();
        }

        var employee = new Employee();

        employee.setFirstname(registrationForm.getFirstname());
        employee.setLastname(registrationForm.getLastname());
        employee.setPatronymic(registrationForm.getPatronymic());
        employee.setEmail(registrationForm.getEmail());
        employee.setTelephone(registrationForm.getTelephone());

        employee.setAuthorities(Collections.singleton(Authority.ROLE_EMPLOYEE));
        employee.setPassword(passwordEncoder.encode(registrationForm.getPassword()));

        employee.setCompany(company);

        employeeRepository.save(employee);
    }
}
