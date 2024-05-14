package ua.wms.warehouse.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Iterable<Product> findAll();

    List<Product> findByNameContaining(String name);


}