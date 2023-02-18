package com.tina.mall2023.service;

import com.tina.mall2023.dto.ProductRequest;
import com.tina.mall2023.model.Product;

public interface ProductService {
    Product getProductById(Integer productID);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productID, ProductRequest productRequest);
    void deleteProductById(Integer productID);
}
