package ru.goryacheva.tacocloud.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.goryacheva.tacocloud.models.Taco;

public interface TacoRepository extends CrudRepository<Taco, Long> {

}
