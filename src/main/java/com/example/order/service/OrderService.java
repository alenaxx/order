package com.example.order.service;

import com.example.order.dao.OrderDao;
import com.example.order.model.OrderDto;
import com.example.order.model.OrderItemDto;
import com.example.order.model.Orders;
import com.example.order.rabbitMQ.RabbitMQPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderDao orderDao;

    @Autowired
    private RabbitMQPublisher rabbitMQPublisher;

    private static List<String> ROUTING_KEYS = Arrays.asList(
            "order.paid",
            "order.cancelled",
            "order.completed"
    );

    @Autowired
    public OrderService(@Qualifier("postgres") OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void deleteOrderById(UUID id){
        orderDao.deleteOrderById(id);
    }
    public void addOrder(Orders order) {
         orderDao.addOrder(order);
    }

    public OrderDto getOrderByID(UUID id)  {
        Orders foundOrder = orderDao.getOrderById(id);
        return new OrderDto(foundOrder, orderDao.getOrderItems(foundOrder));
    }

    public ArrayList<OrderItemDto> getOrderItems(UUID id)  {
        return orderDao.getOrderItems(getOrderByID(id));
    }

    public ArrayList<OrderDto> getOrders() {
        return orderDao.getOrders();
    }

    public OrderDto addItems(UUID id, OrderItemDto item)  {
        return orderDao.addItems(getOrderByID(id), item);
    }


    public OrderDto setOrderStatus(UUID id, String status) {
        OrderDto orderDto = orderDao.setOrderStatus(getOrderByID(id), status);

        if (status.equals("COMPLETED") || status.equals("CANCELLED") || status.equals("PAID")) {
            rabbitMQPublisher.publish("orders." + status.toLowerCase(), orderDto);
        }

        return orderDto;
    }


}

