package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.dto.OrderDTO;
import ua.wms.warehouse.dto.OrderMoveDTO;
import ua.wms.warehouse.entity.*;
import ua.wms.warehouse.repository.OrderRepository;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Order;
import ua.wms.warehouse.entity.OrderStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;

    private final WarehouseService warehouseService;

    private final ProductService productService;

    public List<Order> getAllOrders() {

        return orderRepo.findAll();
    }

    public Order saveNewOrder(Order order) {

        order.setQuantity(order.getProducts().size());
        order.setStatus(OrderStatus.NEW);

        order.getProducts().forEach(product ->
                product.setOrderSet(Collections.singleton(order)));

        return Optional.of(orderRepo.save(order))
                .orElseThrow(() ->
                        new RuntimeException("Помилка при створенні замовлення"));
    }

    public Order findOrder(Long id) {

        return orderRepo.findOrderById(id);
    }

    public List<Order> getOrders(Company company, OrderStatus status) {
        return orderRepo.findAllByCompanyAndStatus(company, status);
    }

    public void changeStatus(OrderDTO orderDTO, OrderStatus orderStatus) {
        var order = orderRepo.findOrderById(orderDTO.getId());
        order.setStatus(orderStatus);

        orderRepo.save(order);
    }

    public Order getById(Long orderId) {

        return orderRepo.findOrderById(orderId);
    }

    public void moveToOtherWarehouse(OrderMoveDTO orderMoveDTO) {
        var order = orderRepo.findOrderById(orderMoveDTO.getOrderId());
        var warehouse = warehouseService.getWarehouseById(orderMoveDTO.getWarehouseId());
        var products = order.getProducts();

        productService.move(products, Optional.ofNullable(warehouse));
    }

}
