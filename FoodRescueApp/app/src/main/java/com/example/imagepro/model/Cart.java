package com.example.imagepro.model;

public class Cart {
    private int cart_id;
    private String cart_name;
    private int cart_quantity;
    private int cart_price;
    private int total_price;

    public Cart(int cart_id, String cart_name, int cart_quantity, int cart_price, int total_price) {
        this.cart_id = cart_id;
        this.cart_name = cart_name;
        this.cart_quantity = cart_quantity;
        this.cart_price = cart_price;
        this.total_price = total_price;
    }

    public Cart() {
    }

    public Cart(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public String getCart_name() {
        return cart_name;
    }

    public void setCart_name(String cart_name) {
        this.cart_name = cart_name;
    }

    public int getCart_quantity() {
        return cart_quantity;
    }

    public void setCart_quantity(int cart_quantity) {
        this.cart_quantity = cart_quantity;
    }

    public int getCart_price() {
        return cart_price;
    }

    public void setCart_price(int cart_price) {
        this.cart_price = cart_price;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }
}


