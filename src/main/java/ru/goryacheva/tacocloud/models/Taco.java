package ru.goryacheva.tacocloud.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table
public class Taco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date createdAt;

    @NotNull
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private  String name;

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany(targetEntity=Ingredient.class)
    private List<Ingredient> ingredients = new ArrayList<>();

    @Transient
    private List<IngredientRef> ingredientRefs = new ArrayList<>();

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }
}
