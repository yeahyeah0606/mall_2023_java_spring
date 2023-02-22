package com.tina.mall2023.dao.impl;

import com.tina.mall2023.dao.OrderDao;
import com.tina.mall2023.dto.OrderQueryParams;
import com.tina.mall2023.model.Order;
import com.tina.mall2023.model.OrderItem;
import com.tina.mall2023.rowmapper.OrderItemRowMapper;
import com.tina.mall2023.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate npjt;

    @Override
    public Integer createOrder(Integer userID, Integer totalAmount) {
        String sql = "INSERT INTO mall.`order` (user_id, total_amount, created_date, last_modified_date) "+
                "VALUES (:userID, :totalAmount, :createdDate, :lastModifiedDate)";

        Map<String, Object> map =  new HashMap<>();
        map.put("userID", userID);
        map.put("totalAmount", totalAmount);

        Date now =  new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        npjt.update(sql, new MapSqlParameterSource(map), keyHolder);

        //int orderID = keyHolder.getKey().intValue();
        return keyHolder.getKey().intValue();
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
        String sql = "INSERT INTO mall.order_item (order_id, product_id, quantity, amount )" +
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

    @Override
    public Order getOrderById(Integer orderID) {
        String sql="SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM mall.`order` WHERE order_id = :orderID ";
        Map<String, Object> map = new HashMap<>();
        map.put("orderID", orderID);
        List<Order> orderList = npjt.query(sql, map, new OrderRowMapper());

        if(orderList.size()>0){
            return orderList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderID(Integer orderID) {
        String sql ="SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " +
                "FROM mall.order_item as oi " +
                "LEFT JOIN mall.product as p ON oi.product_id = p.product_id " +
                "WHERE oi.order_id = :orderID" ;

        Map<String, Object> map = new HashMap<>();
        map.put("orderID", orderID);

        //List<OrderItem> orderItemList = npjt.query(sql, map, new OrderItemRowMapper());
        return npjt.query(sql, map, new OrderItemRowMapper());
    }

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT count(*) FROM mall.`order` WHERE 1=1 ";
        Map<String, Object> map = new HashMap<>();
        // 查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        //Integer total = npjt.queryForObject(sql, map, Integer.class);
        return  npjt.queryForObject(sql, map, Integer.class);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date FROM mall.`order` WHERE 1=1 ";
        Map<String, Object> map = new HashMap<>();
        // 查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);
        //排序
        sql = sql + " ORDER BY created_date DESC ";
        //分頁
        sql =sql +" LIMIT :limit OFFSET :offset ";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());


        //List<Order> orderList = npjt.query(sql, map, new OrderRowMapper());
        return npjt.query(sql, map, new OrderRowMapper());
    }

    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams){
        if(orderQueryParams.getUserID() != null ){
            sql = sql +" AND user_id = :userID ";
            map.put("userID", orderQueryParams.getUserID());
        }
        return  sql;
    }
}
