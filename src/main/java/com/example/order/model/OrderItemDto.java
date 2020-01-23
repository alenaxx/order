package com.example.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class OrderItemDto {


    private UUID itemId;
    private int amount;


    public UUID getItemId() {
        return this.itemId;
    }

    public int getAmount() {
        return this.amount;
    }

    public OrderItemDto(@JsonProperty("itemId") UUID itemId, @JsonProperty("amount") int amount) {
        this.itemId = itemId;
        this.amount = amount;


    }

    public OrderItemDto() {
    }


    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }



}

