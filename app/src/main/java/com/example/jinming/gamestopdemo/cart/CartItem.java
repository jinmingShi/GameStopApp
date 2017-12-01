package com.example.jinming.gamestopdemo.cart;

import com.example.jinming.gamestopdemo.product.ProductItem;

/**
 * Created by Jinming on 11/29/17.
 */

public class CartItem {
    private String quantity;
    private ProductItem productItem;

    public CartItem(ProductItem productItem, String quantity) {
        this.quantity = quantity;
        this.productItem = productItem;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }

    public ProductItem getProductItem() {
        return productItem;
    }


}
