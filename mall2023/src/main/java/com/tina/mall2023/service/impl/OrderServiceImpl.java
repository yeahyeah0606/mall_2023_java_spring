package com.tina.mall2023.service.impl;

import com.tina.mall2023.dao.OrderDao;
import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.dao.UserDao;
import com.tina.mall2023.dto.BuyItem;
import com.tina.mall2023.dto.CreateOrderRequest;
import com.tina.mall2023.model.Order;
import com.tina.mall2023.model.OrderItem;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.model.User;
import com.tina.mall2023.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    private  final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Transactional
    @Override
    public Integer createOrder(Integer userID, CreateOrderRequest createOrderRequest) {
        // 檢查 user 是否存在
        User user = userDao.getUserById(userID);

        if( user == null ){
            log.warn("該 userID {} 不存在", userID);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList =new ArrayList<>();
        for( BuyItem buyItem: createOrderRequest.getBuyItemList() ){
            Product product = productDao.getProductById(buyItem.getProductID());

            // 檢查 product 是否存在、庫存是否足夠
            if( product == null ){
                log.warn("商品 {} 不存在", buyItem.getProductID());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if( product.getStock() < buyItem.getQuantity() ){
                log.warn("商品 {} 庫存數量不足，無法購買，剩餘庫存 {} ，欲購買數量為 {} ",
                        buyItem.getProductID(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            // 扣除並計算剩餘庫存
            productDao.updateStock(product.getProductId(), product.getStock()- buyItem.getQuantity());

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

    @Override
    public Order getOrderById(Integer orderID) {
        Order order = orderDao.getOrderById(orderID);
        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderID(orderID);

        order.setOrderItemList(orderItemList);
        return order;
    }
}
