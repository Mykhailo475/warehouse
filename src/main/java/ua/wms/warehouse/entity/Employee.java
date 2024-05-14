package ua.wms.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;




@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "employee_t")
public class Employee extends User {


}
