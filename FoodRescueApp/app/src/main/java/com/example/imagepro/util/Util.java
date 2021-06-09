package com.example.imagepro.util;

public class Util {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "food_rescue_db_11";

    // User table
    public static final String TABLE_NAME_USER = "user";
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String PASSWORD = "password";

    // Food table
    public static final String TABLE_NAME_FOOD = "food";
    public static final String FOOD_ID = "food_id";
    public static final String USER_ID_FOOD = "user_id";
    public static final String IMAGE = "image";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";
    public static final String DATE = "date";
    public static final String PICK_UP_TIMES = "pick_up_times";
    public static final String QUANTITY = "quantity";
    public static final String LOCATION = "location";

    // Cart table
    public static final String TABLE_NAME_CART = "cart";
    public static final String CART_ID = "cart_id";
    public static final String CART_NAME = "cart_name";
    public static final String CART_QUANTITY = "cart_quantity";
    public static final String CART_PRICE = "cart_price";
    public static final String CART_TOTAL_PRICE = "cart_total_price";

    // Location table
    public static final String TABLE_NAME_LOCATION = "location";
    public static final String LOCATION_ID = "LOCATION_ID";
    public static final String LOCATION_NAME = "LOCATION_NAME";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
}
