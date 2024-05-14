package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.entity.*;
import ua.wms.warehouse.exceptions.NotFoundWarehouseException;
import ua.wms.warehouse.exceptions.OverflowWarehouse;
import ua.wms.warehouse.exceptions.WarehouseException;
import ua.wms.warehouse.repository.ProductRepository;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Product;
import ua.wms.warehouse.entity.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private final WarehouseService warehouseService;

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow( () ->
                        new RuntimeException("Товар не був знайдений за id: " + id));
    }

    public Product updateProduct(Long id, Product editedProduct) {

        return productRepository.findById(id)
                .map(product -> {
                    product.setName(editedProduct.getName());
                    product.setDescription(editedProduct.getDescription());
                    product.setCategory(editedProduct.getCategory());
                    product.setPrice(editedProduct.getPrice());
                    product.setQuantity(editedProduct.getQuantity());

                    return productRepository.save(product);
                })
                .orElseThrow( () ->
                        new RuntimeException("Товар не був знайдений за id: " + id));
    }


    public Optional<Iterable<Product>> getCompanyProducts(Company company) {
        var warehouseList = warehouseService.getCompanyWarehouse(company).get();

        var products = new ArrayList<Product>();

        for (var wh : warehouseList){
            products.addAll(wh.getProductList() );
        }

        return Optional.of(products);
    }

    public void move(Set<Product> products, Optional<Warehouse> warehouse) {
        products.forEach( product ->
                product.setWarehouse(warehouse.get()));

        productRepository.saveAll(products);
    }

    public void createProduct(Product product, Long warehouseId) throws WarehouseException {
        var warehouseOpt = warehouseService.getById(warehouseId);

        if(warehouseOpt.isEmpty())
            throw new NotFoundWarehouseException("Склад не знайено");

        if(product.getQuantity()<0)
            throw new IllegalArgumentException();

        var warehouse = warehouseOpt.get();
        product.setWarehouse(warehouse);

        if(warehouse.getCapacity() < product.getQuantity())
            throw new OverflowWarehouse("Склад не може вмістити стільки товару");

        productRepository.save(product);
    }

    public Optional<Iterable<Product>> getAllProducts() {

        return Optional.ofNullable(productRepository.findAll());
    }

    public Optional<Iterable<Product>> findByNameContaining(String name) {

        return Optional.ofNullable(productRepository.findByNameContaining(name));
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public void saveParserProduct(List<Product> products,Long warehouseId) throws WarehouseException {
        for (var p : products)
            createProduct(p,warehouseId);
    }

}
