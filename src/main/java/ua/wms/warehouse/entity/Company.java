package ua.wms.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "company_t")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "company")
    private Set<Warehouse> warehouses;

    @OneToMany(mappedBy = "company")
    private Set<Order> orders;

    @OneToMany(mappedBy = "company")
    private Set<User> users;

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", warehouses=" + warehouses +
                ", orders=" + orders +
                ", users=" + users +
                '}';
    }
}
