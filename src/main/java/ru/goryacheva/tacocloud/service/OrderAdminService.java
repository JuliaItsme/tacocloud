package ru.goryacheva.tacocloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.goryacheva.tacocloud.repository.jpa.OrderRepository;

@Service
public class OrderAdminService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderAdminService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")  //защищает метод. Его смогут вызвать только с привилегией ADMIN
    public void deleteAllOrders(){
        orderRepository.deleteAll();
    }
}
