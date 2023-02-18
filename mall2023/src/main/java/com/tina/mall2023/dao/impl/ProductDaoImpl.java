package com.tina.mall2023.dao.impl;

import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.dto.ProductRequest;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.rowmapper.ProductRowMapper;
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

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql ="INSERT INTO product(product_name, category, image_url,price, stock," +
                "  description, created_date, last_modified_date)  " +
                "VALUES ( :productName, :category, :imageUrl, :price, :stock, :description, " +
                ":createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString() );
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate",now);
        map.put("lastModifiedDate",now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        npjt.update(sql, new MapSqlParameterSource(map), keyHolder);
        int productID = keyHolder.getKey().intValue();
        return productID;
    }

    @Override
    public void updateProduct(Integer productID, ProductRequest productRequest) {
        String sql ="UPDATE product SET product_name=:productName, category=:category, image_url=:imageUrl, "+
                "price=:price, stock=:stock, description=:description, last_modified_date=:lastModifiedDate " +
                "WHERE product_id=:productID";
        Map<String, Object> map = new HashMap<>();
        map.put("productID", productID);

        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString() );
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate", new Date());
        npjt.update(sql, map);
    }
}
