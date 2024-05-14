package ua.wms.warehouse.controller.order;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.wms.warehouse.dto.OrderDTO;
import ua.wms.warehouse.dto.OrderMoveDTO;
import ua.wms.warehouse.entity.Order;
import ua.wms.warehouse.entity.OrderStatus;
import ua.wms.warehouse.security.UserPrincipal;
import ua.wms.warehouse.service.OrderService;
import ua.wms.warehouse.service.ProductService;
import ua.wms.warehouse.service.WarehouseService;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final WarehouseService warehouseService;

    public OrderController(OrderService orderService, ProductService productService, WarehouseService warehouseService) {
        this.orderService = orderService;
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String orders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model,
            @RequestParam(
                    name = "status",
                    required = false,
                    defaultValue = "NEW")
            OrderStatus status
    ) {
        model.addAttribute("orders", orderService.getOrders(userPrincipal.getUser().getCompany(), status));
        model.addAttribute("orderDTO", new OrderDTO());

        return "order/orders";
    }

    @GetMapping("/manage")
    public String manageOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        model.addAttribute("orders", orderService.getOrders(userPrincipal.getUser().getCompany(), OrderStatus.PROCESSING));
        model.addAttribute("productList", productService.getCompanyProducts(userPrincipal.getUser().getCompany()).get());
        model.addAttribute("order", new Order());

        return "order/manage";
    }

    @GetMapping("/manage/move/{id}")
    public String moveOrdersProduct(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model,
            @PathVariable(name = "id")
            Long orderId
    ) {
        model.addAttribute("order", orderService.getById(orderId));
        model.addAttribute("orderMoveDTO", new OrderMoveDTO());
        model.addAttribute("warehouses", warehouseService.getCompanyWarehouse(userPrincipal.getUser().getCompany()).get());

        return "order/move";
    }


    @PostMapping("/manage/move")
    public String moveOrdersProduct(
            OrderMoveDTO orderMoveDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        orderService.moveToOtherWarehouse(orderMoveDTO);
        return "redirect:/orders";
    }

    @PostMapping("/manage/create")
    public String createOrder(
            @ModelAttribute("order")
            @Valid Order order,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Непрвильні дані замовлення");
        }
        order.setCompany(userPrincipal.getUser().getCompany());
        orderService.saveNewOrder(order);

        return "redirect:/orders";
    }

    @PostMapping("/status/process")
    public String processOrder(
            @ModelAttribute("order")
            @Valid OrderDTO orderDTO
    ) {
        orderService.changeStatus(orderDTO, OrderStatus.PROCESSING);

        return "redirect:/orders";
    }

    @PostMapping("/status/competed")
    public String competedOrder(
            @ModelAttribute("order")
            @Valid OrderDTO orderDTO
    ) {
        orderService.changeStatus(orderDTO, OrderStatus.COMPLETED);

        return "redirect:/orders";
    }


    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {

        return orderService.findOrder(id);
    }

}
