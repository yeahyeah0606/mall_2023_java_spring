package com.tina.mall2023.dao;

import com.tina.mall2023.dto.OrderQueryParams;
import com.tina.mall2023.model.Order;
import com.tina.mall2023.model.OrderItem;

import java.util.List;

public interface OrderDao {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderID);
    List<OrderItem> getOrderItemsByOrderID(Integer orderID);
    Integer createOrder(Integer userID, Integer totalAmount);
    void createOrderItems(Integer orderID, List<OrderItem> orderItemList);

}
