package com.tina.mall2023.service.impl;

import com.tina.mall2023.dao.ProductDao;
import com.tina.mall2023.model.Product;
import com.tina.mall2023.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    public Product getProductById(Integer productID) {
        return productDao.getProductById(productID);
    }
}
