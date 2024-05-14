package ua.wms.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findAllByCompany(Company company);

    boolean existsByTelephone(Long telephone);
    boolean existsByEmail(String email);


}
