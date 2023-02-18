package com.tina.mall2023.dao.impl;

import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate npjt;
    @Override
    public Product getProductById(Integer productID) {
        String sql ="SELECT product_id,product_name, category, image_url," +
                " price, stock, description, created_date, last_modified_date  " +
                "from product where product_id =:productID";
        Map<String, Object> map = new HashMap<>();
        map.put("productID", productID);
        List<Product> productList = npjt.query(sql, map, new ProductRowMapper());
        if(productList.size()>0){
            return productList.get(0);
        }else{
            return  null;
        }
    }
}
