package com.example.order.model;

import java.util.ArrayList;

public class OrderDto extends Orders{

    public ArrayList<OrderItemDto> getItems() {
        return items;
    }

    private final ArrayList<OrderItemDto> items;

    public OrderDto(Orders order, ArrayList<OrderItemDto> items){
        super(order.getId(), order.getTotalAmount(), order.getOrderStatus().ordinal(),
               order.totalCost);
        this.items = items;
    }
}
