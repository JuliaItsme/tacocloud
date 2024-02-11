package ru.goryacheva.tacocloud.repository.jdbctemplate;

import ru.goryacheva.tacocloud.models.TacoOrder;

import java.util.Optional;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);

    Optional<TacoOrder> findById(Long id);
}
