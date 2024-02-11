package ru.goryacheva.tacocloud.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.goryacheva.tacocloud.models.TacoOrder;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);

 //   @Query("Order o where o.deliveryCity = 'Seattle'")
 //   List<TacoOrder> readOrdersDeliveredInSeattle();

//    List<TacoOrder> findByDeliveryToAndDeliveryCityAllIgnoresCase(String deliveryTo, String deliveryCity);

//    List<TacoOrder> findByDeliveryCityOrderByDeliveryTo(String city);
}
