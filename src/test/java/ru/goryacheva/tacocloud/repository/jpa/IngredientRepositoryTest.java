package ru.goryacheva.tacocloud.repository.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.goryacheva.tacocloud.models.Ingredient;
import ru.goryacheva.tacocloud.models.Ingredient.Type;
import ru.goryacheva.tacocloud.repository.jpa.IngredientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IngredientRepositoryTest {

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    JdbcTemplate jdbc;

    @Test
    public void findById() {
        Optional<Ingredient> flto = ingredientRepository.findById("FLTO");
        assertThat(flto.isPresent()).isTrue();
        assertThat(flto.get()).isEqualTo(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));

        Optional<Ingredient> xxxx = ingredientRepository.findById("XXXX");
        assertThat(xxxx.isEmpty()).isTrue();
    }
}
