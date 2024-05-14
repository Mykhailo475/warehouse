package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.entity.*;
import ua.wms.warehouse.repository.WarehouseRepository;
import ua.wms.warehouse.entity.Company;
import ua.wms.warehouse.entity.Warehouse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public Warehouse saveWarehouse(Warehouse warehouse, Company company) {
        warehouse.setCompany(company);

        return Optional.of(warehouseRepository.save(warehouse))
                .orElseThrow(() ->
                        new RuntimeException("Помилка: склад не було збережено "));
    }

    public Warehouse getWarehouseById(Long warehouseId) {

        return Optional.of(warehouseRepository.findById(warehouseId).get())
                .orElseThrow(() ->
                        new RuntimeException("Помилка: склад не знайдено "));
    }

    public Optional<Warehouse> getById(Long id) {

        return Optional.of(warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Помилка: склад не знайдено за id :" + id)));
    }

    public Optional<Iterable<Warehouse>> getCompanyWarehouse(Company company) {
        var warehouseList = warehouseRepository.findAllByCompany(company);
        return Optional.ofNullable(warehouseList);
    }

    public Optional<Iterable<Warehouse>> findByNameContaining(String name) {

        return Optional.ofNullable(warehouseRepository.findByNameContaining(name));
    }

    public void deleteWarehouseById(Long id) {
        warehouseRepository.deleteById(id);
    }

    public Optional<Iterable<Warehouse>> findAllById(Long warehouseId) {
        return Optional.ofNullable(warehouseRepository.findAllById(warehouseId));
    }

}
