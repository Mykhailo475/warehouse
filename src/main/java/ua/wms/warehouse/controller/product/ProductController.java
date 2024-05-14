package ua.wms.warehouse.controller.product;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.wms.warehouse.entity.Product;
import ua.wms.warehouse.entity.Warehouse;
import ua.wms.warehouse.exceptions.NotFoundWarehouseException;
import ua.wms.warehouse.exceptions.OverflowWarehouse;
import ua.wms.warehouse.exceptions.WarehouseException;
import ua.wms.warehouse.security.UserPrincipal;
import ua.wms.warehouse.service.ProductService;
import ua.wms.warehouse.service.WarehouseService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final WarehouseService warehouseService;

    public ProductController(ProductService productService, WarehouseService warehouseService) {
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("/addProduct")
    public String addProduct(
            @RequestParam(required = true) Long warehouseId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            Model model
    ) {
        var user = userPrincipal.getUser().getCompany();
        Optional<Iterable<Warehouse>> warehouseList = warehouseService.findAllById(warehouseId);

        model.addAttribute("warehouses", warehouseList);
        model.addAttribute("warehouseId", warehouseId);
        model.addAttribute("product", new Product());

        return "product/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute("product")
            @Valid Product product,
            @Valid Long warehouse,
            BindingResult bindingResult
    ) {
        Optional.of(bindingResult)
                .filter(BindingResult::hasErrors)
                .ifPresent(errors -> {
                    throw new IllegalArgumentException("Неправильні дані про товар");
                });

        try {
            productService.createProduct(
                    product,
                    warehouse
            );
        } catch (NotFoundWarehouseException e) {
            log.error("NotFound Warehouse");
            return String.format("redirect:/products/addProduct?warehouseId=%s&notFound", warehouse);
        } catch (OverflowWarehouse e) {
            log.error("Overflow Warehouse");
            return String.format("redirect:/products/addProduct?warehouseId=%s&overflow", warehouse);
        } catch (WarehouseException e) {
            log.error("Warehouse Exception");
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            log.error("Illegal Argument Exception");
            return String.format("redirect:/products/addProduct?warehouseId=%s&illegal", warehouse);
        }

        return "redirect:/warehouses";
    }

    @GetMapping("/search")
    public String getProductsByName(
            @RequestParam(
                    name = "name",
                    required = false,
                    defaultValue = "")
            Optional<String> nameFilterOpt,
            @RequestParam(
                    name = "warehouseId",
                    required = false,
                    defaultValue = "")
            Optional<Long> warehouseId,
            Model model
    ) {
        Optional<Iterable<Product>> productList = Optional.of(
                nameFilterOpt
                        .filter(filter -> !filter.isEmpty())
                        .flatMap(productService::findByNameContaining)
                        .orElseGet(() -> productService.getAllProducts().orElse(new ArrayList<>()))
        );

        model.addAttribute("products", productList.get());
        model.addAttribute("product", new Product());

        return "product/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        productService.deleteProductById(id);

        var referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @PostMapping("/{id}/edit")
    public String saveEditedProduct(
            @PathVariable Long id,
            @ModelAttribute Product editedProduct,
            HttpServletRequest request
    ) {
        productService.updateProduct(id, editedProduct);

        var referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @PostMapping("/filter")
    public String filterProducts(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(defaultValue = "") String filter,
            Model model
    ) {
        Optional<Iterable<Product>> productList = productService.getAllProducts();

        productList.ifPresent(products -> {
            if (sort.equals("asc"))
                products = StreamSupport.stream(products.spliterator(), false)
                        .sorted(Comparator.comparing(Product::getPrice))
                        .collect(Collectors.toList());

            else if (sort.equals("desc"))
                products = StreamSupport.stream(products.spliterator(), false)
                        .sorted(Comparator.comparing(Product::getPrice).reversed())
                        .collect(Collectors.toList());

            model.addAttribute("products", products);
        });

        model.addAttribute("product", new Product());

        return "product/products";
    }
}
