package com.example.imagepro.model;

public class Food {
    private int food_id;
    private int user_id;
    private String image;
    private String title;
    private String description;
    private int price;
    private String date;
    private String pickUpTimes;
    private int quantity;
    private String location;

    public Food(int food_id, int user_id, String image, String title, String description, int price, String date, String pickUpTimes, int quantity, String location) {
        this.food_id = food_id;
        this.user_id = user_id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.price = price;
        this.date = date;
        this.pickUpTimes = pickUpTimes;
        this.quantity = quantity;
        this.location = location;
    }

    public Food() {

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPickUpTimes() {
        return pickUpTimes;
    }

    public void setPickUpTimes(String pickUpTimes) {
        this.pickUpTimes = pickUpTimes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
