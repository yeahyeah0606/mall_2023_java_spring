package com.tina.mall2023.service.impl;

import com.tina.mall2023.dao.OrderDao;
import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.dto.BuyItem;
import com.tina.mall2023.dto.CreateOrderRequest;
import com.tina.mall2023.model.OrderItem;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Transactional
    @Override
    public Integer createOrder(Integer userID, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList =new ArrayList<>();
        for( BuyItem buyItem: createOrderRequest.getBuyItemList() ){
            Product product = productDao.getProductById(buyItem.getProductID());

            // 計算總價錢
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount = totalAmount + amount;

            // 轉換 BuyItem to OrderItem
            OrderItem orderItem =new OrderItem();
            orderItem.setProductID(buyItem.getProductID());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        // 創建訂單
        Integer orderID = orderDao.createOrder(userID, totalAmount);

        orderDao.createOrderItems(orderID, orderItemList);

        return orderID;
    }
}
