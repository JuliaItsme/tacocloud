package ru.goryacheva.tacocloud.repository.jdbctemplate.impl;

import ru.goryacheva.tacocloud.models.Ingredient;
import ru.goryacheva.tacocloud.repository.jdbctemplate.IngredientRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RawJdbcIngredientRepository implements IngredientRepository {

    private final DataSource dataSource;

    public RawJdbcIngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT id, name, type FROM Ingredient");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Ingredient ingredient = new Ingredient(resultSet.getString("id"),
                        resultSet.getString("name"),
                        Ingredient.Type.valueOf(resultSet.getString("type")));
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            // что-то делать если возникнет исключение
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return ingredients;
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement("SELECT id, name, type FROM Ingredient WHERE id=?");
            statement.setString(1, id);
            resultSet = statement.executeQuery();
            Ingredient ingredient = null;
            if (resultSet.next()) {
                ingredient = new Ingredient(resultSet.getString("id"),
                        resultSet.getString("name"),
                        Ingredient.Type.valueOf(resultSet.getString("type")));
            }
            return Optional.of(ingredient);
        } catch (SQLException e) {
            // что-то делать если возникнет исключение
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        //пока не реализовываем
        return null;
    }
}