package com.tina.mall2023.dao;

import com.tina.mall2023.model.Product;

public interface ProductDao {
    Product getProductById(Integer productID);
}
