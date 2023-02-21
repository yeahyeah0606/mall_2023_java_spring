package com.tina.mall2023.controller;

import com.tina.mall2023.dto.CreateOrderRequest;
import com.tina.mall2023.model.Order;
import com.tina.mall2023.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/users/{userID}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userID,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
        Integer orderID = orderService.createOrder(userID, createOrderRequest);

        Order order = orderService.getOrderById(orderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
