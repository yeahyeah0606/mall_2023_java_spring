package com.tina.mall2023.dao;

import com.tina.mall2023.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer createOrder(Integer userID, Integer totalAmount);
    void createOrderItems(Integer orderID, List<OrderItem> orderItemList);

}
