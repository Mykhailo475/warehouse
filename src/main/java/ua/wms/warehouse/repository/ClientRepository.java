package ua.wms.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
