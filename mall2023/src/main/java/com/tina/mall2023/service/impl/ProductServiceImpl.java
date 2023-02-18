package com.tina.mall2023.service.impl;

import com.tina.mall2023.constant.ProductCategory;
import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.dto.ProductRequest;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getProducts(ProductCategory category, String search) {
        return productDao.getProducts(category, search);
    }

    @Override
    public Product getProductById(Integer productID) {
        return productDao.getProductById(productID);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productID, ProductRequest productRequest) {
        productDao.updateProduct(productID, productRequest);
    }

    @Override
    public void deleteProductById(Integer productID) {
        productDao.deleteProductById(productID);
    }
}
