package ua.wms.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.wms.warehouse.entity.User;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepository<T extends User, E extends Serializable> extends JpaRepository<T, E> {

    Optional<T> findByEmail(String email);
    boolean existsByTelephone(Long telephone);
    boolean existsByEmail(String email);

    Optional<T> findById(E id);

}
