package com.example.order.dao;

import com.example.order.model.*;

import java.util.ArrayList;
import java.util.UUID;

public interface OrderDao {

 void addOrder(UUID id, Orders order);

    default void addOrder(Orders order) {
        UUID id = UUID.randomUUID();
       addOrder(id, order);
    }

    ArrayList<OrderDto>  getAllOrders();


    OrderDto addItems(Orders order, OrderItemDto items);

    OrderDto setOrderStatus(Orders order, String status);

    ArrayList<OrderDto> getOrders();

    Orders getOrderById(UUID id);

     ArrayList<OrderItemDto> getOrderItems(Orders order);
}
