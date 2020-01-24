package com.example.order.dao;

import com.example.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository("postgres")
public class OrderDaoService implements OrderDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDaoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addOrder(UUID id, Orders order) {
        jdbcTemplate.update(
                "INSERT INTO orders ( id, orderStatus,totalCost) VALUES (?, ?, ?)",
                id, order.getOrderStatus().ordinal(), order.getTotalCost()
        );


    }



    @Override
    public ArrayList<OrderDto> getAllOrders() {
        return null;
    }

    @Override
    public void deleteOrderById(UUID id) {
        String deleteSql = "DELETE  FROM orderItems WHERE orderId=?";
        jdbcTemplate.update(deleteSql,
                id);
        String deleteSql2 = "DELETE  FROM orders WHERE id=?";
        jdbcTemplate.update(deleteSql2,
                id);
    }

    @Override
    public ArrayList<OrderDto> getOrders() {
        final String sql = "SELECT * FROM orders";
        List<Map<String, Object>> order = jdbcTemplate.queryForList(sql);
        ArrayList<OrderDto> orderDtos = new ArrayList<>();
        for (Map<String, Object> map : order) {

            orderDtos.add(new OrderDto(getOrderById((UUID) map.get("id")), getOrderItems(getOrderById((UUID) map.get("id")))));

        }
return orderDtos;
    }


    public OrderDto addItems(Orders order, OrderItemDto item) {
        jdbcTemplate.update(
                "INSERT INTO orderItems(  itemId,orderId , amount) VALUES (?,?,?)",
                item.getItemId(), order.getId(), item.getAmount()
        );
        return new OrderDto(order, getOrderItems(order));
    }

    public ArrayList<OrderItemDto> getOrderItems(Orders order) {
        return selectItems(order.getId());
    }

    private ArrayList<OrderItemDto> selectItems(UUID id) {
        ArrayList<OrderItemDto> orderItems = new ArrayList<>();
        final String sql = "SELECT itemId, amount FROM orderItems WHERE orderId =?";
        List<Map<String, Object>> resultSet = jdbcTemplate
                .queryForList(sql, id);
        for (Map res : resultSet) {
            OrderItemDto orderItem = new OrderItemDto();
            orderItem.setAmount((int) (res.get("amount")));
            orderItem.setItemId((UUID) (res.get("itemId")));
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    @Override
    public OrderDto setOrderStatus(Orders order, String status) {
        switch (status) {
            case ("COLLECTING"):
                return putOrderStatus(order, OrderStatus.COLLECTING);
            case ("PAID"):
                return putOrderStatus(order, OrderStatus.PAID);
            case ("SHIPPING"):
                return putOrderStatus(order, OrderStatus.SHIPPING);
            case ("COMPLETED"):
                return putOrderStatus(order, OrderStatus.COMPLETED);
            case ("FAILED"):
                return putOrderStatus(order, OrderStatus.FAILED);
            case ("CANCELLED"):
                return putOrderStatus(order, OrderStatus.CANCELLED);
            default:

        }
        return null;
    }
    private OrderDto putOrderStatus(Orders order, OrderStatus status)  {
        String sql = "UPDATE orders SET   orderStatus = ?  WHERE id =?";
        jdbcTemplate.update(sql,
                status.ordinal(),order.getId());
        order.setOrderStatus(status);
        // order.setOrderStatus(status);
        return new OrderDto(order, getOrderItems(order));
    }

    @Override
    public Orders getOrderById(UUID id) {
        String sql = "SELECT * FROM orders  WHERE id =?";
        Orders order = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, i) -> {
            UUID id1 = UUID.fromString(resultSet.getString("id"));
            Integer orderStatus = resultSet.getInt("orderStatus");
            float totalCost = resultSet.getFloat("totalCost");
            return new Orders(id1, orderStatus, totalCost);
        });
        return order;
    }


}
