package com.tina.mall2023.dao;

import com.tina.mall2023.constant.ProductCategory;
import com.tina.mall2023.dto.ProductQueryParams;
import com.tina.mall2023.dto.ProductRequest;
import com.tina.mall2023.model.Product;

import java.util.List;

public interface ProductDao {
    Integer countProduct(ProductQueryParams productQueryParams);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productID);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productID, ProductRequest productRequest);
    void deleteProductById(Integer productID);
}
