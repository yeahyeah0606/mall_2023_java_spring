package com.tina.mall2023.dao.impl;

import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.dto.ProductQueryParams;
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
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM mall.product WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql =addFilteringSql(sql, map, productQueryParams);
        //Integer total = npjt.queryForObject(sql, map, Integer.class);
        return   npjt.queryForObject(sql, map, Integer.class);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql ="SELECT product_id,product_name, category, image_url," +
                " price, stock, description, created_date, last_modified_date  " +
                "from mall.product where 1=1";
        Map<String, Object> map = new HashMap<>();

        // 查詢條件
        sql =addFilteringSql(sql, map, productQueryParams);

        // 排序
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " +
                productQueryParams.getSort() ;
        // 分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        //List<Product> productList = npjt.query(sql, map, new ProductRowMapper());
        return npjt.query(sql, map, new ProductRowMapper());
    }

    @Override
    public Product getProductById(Integer productID) {
        String sql ="SELECT product_id,product_name, category, image_url," +
                " price, stock, description, created_date, last_modified_date  " +
                "from mall.product where product_id =:productID";
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
        String sql ="INSERT INTO mall.product(product_name, category, image_url,price, stock," +
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
        //int productID = keyHolder.getKey().intValue();
        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateProduct(Integer productID, ProductRequest productRequest) {
        String sql ="UPDATE mall.product SET product_name=:productName, category=:category, image_url=:imageUrl, "+
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

    @Override
    public void updateStock(Integer productID, Integer stock) {
        String sql = "UPDATE mall.product SET stock = :stock, last_modified_date = :lastModifiedDate" +
                " WHERE product_id = :productID ";
        Map<String, Object> map = new HashMap<>();
        map.put("productID", productID);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());

        npjt.update(sql, map);

    }

    @Override
    public void deleteProductById(Integer productID) {
        String sql = "DELETE FROM mall.product WHERE product_id=:productID";
        Map<String, Object> map = new HashMap<>();
        map.put("productID", productID);
        npjt.update(sql, map);
    }

    private  String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams){
        if( productQueryParams.getCategory() != null ){
            sql = sql +"  AND  category=:category";
            map.put("category", productQueryParams.getCategory().name());
        }
        if(productQueryParams.getSearch() != null ){
            sql = sql +"  AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() +"%");
        }
        return sql;
    }

}
