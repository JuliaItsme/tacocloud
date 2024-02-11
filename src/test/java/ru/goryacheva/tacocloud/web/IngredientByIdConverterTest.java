package ru.goryacheva.tacocloud.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.goryacheva.tacocloud.models.Ingredient;
import ru.goryacheva.tacocloud.repository.jpa.IngredientRepository;
import ru.goryacheva.tacocloud.web.converter.IngredientByIdConverter;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.goryacheva.tacocloud.models.Ingredient.Type.CHEESE;

public class IngredientByIdConverterTest {

    private IngredientByIdConverter converter;

    @BeforeEach
    public void setup() {
        IngredientRepository ingredientRepo = mock(IngredientRepository.class);
        when(ingredientRepo.findById("AAAA"))
                .thenReturn(Optional.of(new Ingredient("AAAA", "TEST INGREDIENT", CHEESE)));
        when(ingredientRepo.findById("ZZZZ"))
                .thenReturn(Optional.empty());

        this.converter = new IngredientByIdConverter(ingredientRepo);
    }

    @Test
    public void shouldReturnValueWhenPresent() {
        assertThat(converter.convert("AAAA"))
                .isEqualTo(new Ingredient("AAAA", "TEST INGREDIENT", CHEESE));
    }

    @Test
    public void shouldReturnNullWhenMissing() {
        assertThat(converter.convert("ZZZZ")).isNull();
    }
}
