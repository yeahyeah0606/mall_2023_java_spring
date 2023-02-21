package com.tina.mall2023.dao.impl;

import com.tina.mall2023.dao.OrderDao;
import com.tina.mall2023.model.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDatImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate npjt;

    @Override
    public Integer createOrder(Integer userID, Integer totalAmount) {
        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) "+
                "VALUES (:userID, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> map =  new HashMap<>();
        map.put("userID", userID);
        map.put("totalAmount", totalAmount);

        Date now =  new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        npjt.update(sql, new MapSqlParameterSource(map), keyHolder);

        int orderID = keyHolder.getKey().intValue();
        return orderID;
    }

    @Override
    public void createOrderItems(Integer orderID, List<OrderItem> orderItemList) {
//        // 使用 for loop 一條一條 sql 加入數據，效率低
//        for(OrderItem orderItem :  orderItemList){
//            String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount )" +
//                    "VALUES (:orderID, :productID, :quantity, :amount)";
//            Map<String, Object> map =  new HashMap<>();
//            map.put("orderID", orderID);
//            map.put("productID", orderItem.getProductID());
//            map.put("quantity", orderItem.getQuantity());
//            map.put("amount", orderItem.getAmount());
//
//            npjt.update(sql, map);
//        }

        // 使用 batchUpdate 一次加入數據，效率較高
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount )" +
                "VALUES (:orderID, :productID, :quantity, :amount)";
        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];
        for (int i = 0; i < orderItemList.size(); i++){
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderID", orderID);
            parameterSources[i].addValue("productID", orderItem.getProductID());
            parameterSources[i].addValue("quantity", orderItem.getQuantity());
            parameterSources[i].addValue("amount", orderItem.getAmount());
        }
        npjt.batchUpdate(sql, parameterSources);
    }
}
