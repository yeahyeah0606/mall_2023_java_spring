package com.tina.mall2023.dto;

import javax.validation.constraints.NotNull;

public class BuyItem {
    @NotNull
    private  Integer productID;
    @NotNull
    private  Integer quantity;

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
