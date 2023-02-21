package com.tina.mall2023.service;

import com.tina.mall2023.dto.CreateOrderRequest;
import com.tina.mall2023.model.Order;

public interface OrderService {
    Order getOrderById(Integer orderID);
    Integer createOrder(Integer userID, CreateOrderRequest createOrderRequest);
}
