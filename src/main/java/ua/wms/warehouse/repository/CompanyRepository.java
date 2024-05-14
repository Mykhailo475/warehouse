package ua.wms.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
}
