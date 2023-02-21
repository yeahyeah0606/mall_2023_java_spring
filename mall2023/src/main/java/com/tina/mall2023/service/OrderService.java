package com.tina.mall2023.service;

import com.tina.mall2023.dto.CreateOrderRequest;

public interface OrderService {
    Integer createOrder(Integer userID, CreateOrderRequest createOrderRequest);
}
