package ua.wms.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "order_t")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String description;

    @Column(name = "creation_date", columnDefinition = "DATE")
    @CreationTimestamp
    private LocalDateTime creationDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    public boolean isNEW(){

        return status == OrderStatus.NEW;
    }

    public boolean isPROCESSING() {

        return status == OrderStatus.PROCESSING;
    }

    public boolean isCOMPLETED() {

        return status == OrderStatus.COMPLETED;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                ", company=" + company +
                ", products=" + products +
                '}';
    }
}
