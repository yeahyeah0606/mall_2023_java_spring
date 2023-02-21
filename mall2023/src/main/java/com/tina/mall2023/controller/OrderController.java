package com.tina.mall2023.controller;

import com.tina.mall2023.dto.CreateOrderRequest;
import com.tina.mall2023.dto.OrderQueryParams;
import com.tina.mall2023.model.Order;
import com.tina.mall2023.service.OrderService;
import com.tina.mall2023.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/users/{userID}/orders")
    public  ResponseEntity<Page<Order>> getOrders(
            @PathVariable Integer userID,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        OrderQueryParams orderQueryParams = new  OrderQueryParams();
        orderQueryParams.setUserID(userID);
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);

        // 取得 order list
        List<Order> orderList = orderService.getOrders(orderQueryParams);

        // 取得 order總數
        Integer count = orderService.countOrder(orderQueryParams);

        // 分頁
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResults(orderList);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
    @PostMapping("/users/{userID}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userID,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
        Integer orderID = orderService.createOrder(userID, createOrderRequest);

        Order order = orderService.getOrderById(orderID);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
}
