package ua.wms.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Order;
import ua.wms.warehouse.entity.OrderStatus;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderById(Long id);

    List<Order> findAllByCompanyAndStatus(Company company, OrderStatus status);

}
