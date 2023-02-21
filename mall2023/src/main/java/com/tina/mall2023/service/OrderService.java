package com.tina.mall2023.service;

import com.tina.mall2023.dto.CreateOrderRequest;
import com.tina.mall2023.dto.OrderQueryParams;
import com.tina.mall2023.model.Order;

import java.util.List;

public interface OrderService {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderID);
    Integer createOrder(Integer userID, CreateOrderRequest createOrderRequest);
}
