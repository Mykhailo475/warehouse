package ua.wms.warehouse.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderMoveDTO {

    private Long orderId;

    private Long warehouseId;

}
