package com.example.order.api;

import com.example.order.model.OrderItemDto;
import com.example.order.model.Orders;
import com.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void addOrder(@RequestBody Orders order) {
        orderService.addOrder(order);

    }


    @PostMapping("/{id}/items")
    public ResponseEntity addItem(@PathVariable("id") UUID id, @RequestBody OrderItemDto item) {

            return ResponseEntity.ok(orderService.addItems(id, item));

    }

    @GetMapping
    public ResponseEntity getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @PutMapping("/{id}/status/{status}")

    public ResponseEntity setOrderStatus(@PathVariable("id") UUID id, @PathVariable("status") String status) {
        return ResponseEntity.ok(orderService.setOrderStatus(id, status));
    }

}