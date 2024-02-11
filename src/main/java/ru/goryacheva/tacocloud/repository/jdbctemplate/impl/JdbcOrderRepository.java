package ru.goryacheva.tacocloud.repository.jdbctemplate.impl;

import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.goryacheva.tacocloud.models.IngredientRef;
import ru.goryacheva.tacocloud.models.Taco;
import ru.goryacheva.tacocloud.models.TacoOrder;
import ru.goryacheva.tacocloud.repository.jdbctemplate.OrderRepository;

import java.sql.Types;
import java.util.*;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcOperations jdbcOperations;

    @Autowired
    public JdbcOrderRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override

    @Transactional
    public TacoOrder save(TacoOrder order) {
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "INSERT INTO Taco_Order (delivery_name, delivery_street, delivery_city,delivery_state, " +
                        "delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at) VALUES ( ?,?,?,?,?,?,?,?,? )",
                Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP);
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);
        order.setPlacedAt(new Date());
        PreparedStatementCreator preparedStatementCreator = preparedStatementCreatorFactory.newPreparedStatementCreator(
                Arrays.asList(
                        order.getDeliveryName(),
                        order.getDeliveryStreet(),
                        order.getDeliveryCity(),
                        order.getDeliveryState(),
                        order.getDeliveryZip(),
                        order.getCcNumber(),
                        order.getCcExpiration(),
                        order.getCcCVV(),
                        order.getPlacedAt()
                )
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(preparedStatementCreator, keyHolder);
        long orderId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        order.setId(orderId);
        List<Taco> tacoList = order.getTacos();
        int i = 0;
        for (Taco taco : tacoList) {
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    @Override
    public Optional<TacoOrder> findById(Long id) {
        try {
            TacoOrder order = jdbcOperations.queryForObject(
                    "SELECT id, delivery_name, delivery_street, delivery_city, delivery_state, delivery_zip, " +
                            "cc_number, cc_expiration, cc_cvv, placed_at FROM taco_order WHERE id=?",
                    (row, rowNum) -> {
                TacoOrder tacoOrder = new TacoOrder();
                tacoOrder.setId(row.getLong("id"));
                tacoOrder.setDeliveryName(row.getString("delivery_name"));
                tacoOrder.setDeliveryStreet(row.getString("delivery_street"));
                tacoOrder.setDeliveryCity(row.getString("delivery_city"));
                tacoOrder.setDeliveryState(row.getString("delivery_state"));
                tacoOrder.setDeliveryZip(row.getString("delivery_zip"));
                tacoOrder.setCcNumber(row.getString("cc_number"));
                tacoOrder.setCcExpiration(row.getString("cc_expiration"));
                tacoOrder.setCcCVV(row.getString("cc_cvv"));
                tacoOrder.setPlacedAt(new Date(row.getTimestamp("placed_at").getTime()));
                tacoOrder.setTacos(findTacosByOrderId(row.getLong("id")));
                return tacoOrder;}
                    , id);
            return Optional.of(order);
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    private List<Taco> findTacosByOrderId(long orderId) {
        return jdbcOperations.query(
                "SELECT id, name, created_at FROM Taco WHERE taco_order=? ORDER BY taco_order_key",
                (row, rowNum) -> {
            Taco taco = new Taco();
            taco.setId(row.getLong("id"));
            taco.setName(row.getString("name"));
            taco.setCreatedAt(new Date(row.getTimestamp("created_at").getTime()));
            taco.setIngredientRefs(findIngredientsByTacoId(row.getLong("id")));
            return taco;},
                orderId);
    }

    private List<IngredientRef> findIngredientsByTacoId(long tacoId) {
        return jdbcOperations.query("SELECT ingredient FROM Ingredient_Ref WHERE taco = ? ORDER BY taco_key",
                (row, rowNum) -> new IngredientRef(row.getString("ingredient")), tacoId);
    }

    private long saveTaco(long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "INSERT INTO TACO(NAME, CREATED_AT, TACO_ORDER, TACO_ORDER_KEY) VALUES ( ?, ?, ?, ? )",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG);
        pscf.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(taco.getName(),
                taco.getCreatedAt(), orderId, orderKey));
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        taco.setId(tacoId);
        saveIngredientRefs(tacoId, taco.getIngredientRefs());
        return tacoId;
    }

    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef : ingredientRefs) {
            jdbcOperations.update("INSERT INTO INGREDIENT_REF(ingredient, taco, taco_key) VALUES ( ?, ?, ? )",
                    ingredientRef.getIngredient(), tacoId, key++);
        }
    }
}