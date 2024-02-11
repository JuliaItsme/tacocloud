package ru.goryacheva.tacocloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import ru.goryacheva.tacocloud.models.TacoOrder;
import ru.goryacheva.tacocloud.repository.jpa.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostAuthorize("hasRole('ADMIN') || returnObject.user.username == authentication.name")
    public TacoOrder getOrder(long id){
        return orderRepository.findById(id).get();
    }
}
