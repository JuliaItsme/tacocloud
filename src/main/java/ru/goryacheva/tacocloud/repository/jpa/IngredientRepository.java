package ru.goryacheva.tacocloud.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import ru.goryacheva.tacocloud.models.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{
}
