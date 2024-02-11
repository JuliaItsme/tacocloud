package ru.goryacheva.tacocloud.repository.jdbctemplate;

import ru.goryacheva.tacocloud.models.Ingredient;

import java.util.Optional;

public interface IngredientRepository {
    Iterable<Ingredient> findAll();

    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);
}