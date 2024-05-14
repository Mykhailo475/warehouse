package ua.wms.warehouse.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Warehouse;

@Repository
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {

    Iterable<Warehouse> findAllByCompany(Company company);
    Iterable<Warehouse> findAllById(Long id);

    Iterable<Warehouse> findAll();

    Iterable<Warehouse> findByNameContaining(String name);


    @Query(value = "SELECT * FROM warehouse_t ORDER BY creation_date", nativeQuery = true)
    Iterable<Warehouse> sortedByDate();

    @Query(value = "SELECT * FROM warehouse_t ORDER BY  capacity", nativeQuery = true)
    Iterable<Warehouse> sortedByCapacity();

    @Query(value = "SELECT * FROM warehouse_t ORDER BY  address",nativeQuery = true)
    Iterable<Warehouse> sortedByAddress();

    @Query(value = "SELECT * FROM warehouse_t ORDER BY  name",nativeQuery = true)
    Iterable<Warehouse> sortedByName();


}
