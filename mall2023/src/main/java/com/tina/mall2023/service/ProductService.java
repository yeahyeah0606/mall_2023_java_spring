package com.tina.mall2023.service;

import com.tina.mall2023.constant.ProductCategory;
import com.tina.mall2023.dto.ProductRequest;
import com.tina.mall2023.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> getProducts(ProductCategory category, String search);
    Product getProductById(Integer productID);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productID, ProductRequest productRequest);
    void deleteProductById(Integer productID);
}
