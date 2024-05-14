package ua.wms.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Entity(name = "client_t")
public class Client extends User {
}