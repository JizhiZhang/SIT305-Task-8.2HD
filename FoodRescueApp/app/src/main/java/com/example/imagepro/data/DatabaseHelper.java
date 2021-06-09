package com.example.imagepro.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.imagepro.model.Cart;
import com.example.imagepro.model.Food;
import com.example.imagepro.model.Location;
import com.example.imagepro.model.User;
import com.example.imagepro.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME_USER + "(" + Util.USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.NAME + " TEXT, "
                + Util.EMAIL + " TEXT, "
                + Util.PHONE + " TEXT, "
                + Util.ADDRESS + " TEXT, "
                + Util.PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_FOOD_TABLE = "CREATE TABLE " + Util.TABLE_NAME_FOOD + "(" + Util.FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.USER_ID_FOOD + " INTEGER,"
                + Util.IMAGE + " TEXT, "
                + Util.TITLE + " TEXT, "
                + Util.DESCRIPTION + " TEXT, "
                + Util.PRICE + " INTEGER, "
                + Util.DATE + " INTEGER, "
                + Util.PICK_UP_TIMES + " TEXT, "
                + Util.QUANTITY + " INTEGER, "
                + Util.LOCATION + " TEXT)";
        db.execSQL(CREATE_FOOD_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + Util.TABLE_NAME_CART + "(" + Util.CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.CART_NAME + " TEXT, "
                + Util.CART_QUANTITY + " INTEGER, "
                + Util.CART_PRICE + " INTEGER, "
                + Util.CART_TOTAL_PRICE + " INTEGER)";
        db.execSQL(CREATE_CART_TABLE);

        // Test: create location table
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + Util.TABLE_NAME_LOCATION + "(" + Util.LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Util.LOCATION_NAME + " TEXT, "
                + Util.LATITUDE + " TEXT, "
                + Util.LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_USER_TABLE = " DROP TABLE IF EXISTS ";
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_USER});
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_FOOD});
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_CART});
        db.execSQL(DROP_USER_TABLE, new String[]{Util.TABLE_NAME_LOCATION});
        onCreate(db);
    }

    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.NAME, user.getName());
        contentValues.put(Util.EMAIL, user.getEmail());
        contentValues.put(Util.PHONE, user.getPhone());
        contentValues.put(Util.ADDRESS, user.getAddress());
        contentValues.put(Util.PASSWORD, user.getPassword());
        long newRowId = db.insert(Util.TABLE_NAME_USER, null, contentValues);
        db.close();
        return newRowId;
    }

    public int fetchUser(String name, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_USER, new String[]{Util.USER_ID}, Util.EMAIL + "=? and " + Util.PASSWORD + "=?",
                new String[]{name, password}, null, null, null);

        if (cursor.moveToFirst()) {
            db.close();
            return cursor.getInt(cursor.getColumnIndex(Util.USER_ID));
        } else {
            db.close();
            return -1;
        }
    }

    public long insertFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.USER_ID_FOOD, food.getUser_id());
        contentValues.put(Util.IMAGE, food.getImage());
        contentValues.put(Util.TITLE, food.getTitle());
        contentValues.put(Util.DESCRIPTION, food.getDescription());
        contentValues.put(Util.PRICE, food.getPrice());
        contentValues.put(Util.DATE, food.getDate());
        contentValues.put(Util.PICK_UP_TIMES, food.getPickUpTimes());
        contentValues.put(Util.QUANTITY, food.getQuantity());
        contentValues.put(Util.LOCATION, food.getLocation());
        long newRowId = db.insert(Util.TABLE_NAME_FOOD, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Food> fetchAllFood() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = " SELECT * FROM " + Util.TABLE_NAME_FOOD;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setUser_id(cursor.getInt(cursor.getColumnIndex(Util.USER_ID)));
                food.setImage(cursor.getString(cursor.getColumnIndex(Util.IMAGE)));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.TITLE)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION)));
                food.setPrice(cursor.getInt(cursor.getColumnIndex(Util.PRICE)));
                food.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.QUANTITY)));
                food.setPickUpTimes(cursor.getString(cursor.getColumnIndex(Util.PICK_UP_TIMES)));
                food.setLocation(cursor.getString(cursor.getColumnIndex(Util.LOCATION)));
                food.setDate(cursor.getString(cursor.getColumnIndex(Util.DATE)));

                foodList.add(food);
            } while (cursor.moveToNext());
        }
        return foodList;
    }

    public List<Food> fetchAllFood(int userId) {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = "SELECT * FROM " + Util.TABLE_NAME_FOOD + " WHERE " + Util.USER_ID + "= ?";
        Cursor cursor = db.rawQuery(selectAll, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setUser_id((cursor.getInt(cursor.getColumnIndex(Util.USER_ID))));
                food.setImage(cursor.getString(cursor.getColumnIndex(Util.IMAGE)));
                food.setTitle(cursor.getString(cursor.getColumnIndex(Util.TITLE)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION)));
                food.setDescription(cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION)));
                food.setPrice(cursor.getInt(cursor.getColumnIndex(Util.PRICE)));
                food.setQuantity(cursor.getInt(cursor.getColumnIndex(Util.QUANTITY)));
                food.setPickUpTimes(cursor.getString(cursor.getColumnIndex(Util.PICK_UP_TIMES)));
                food.setLocation(cursor.getString(cursor.getColumnIndex(Util.LOCATION)));
                food.setDate(cursor.getString(cursor.getColumnIndex(Util.DATE)));
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        return foodList;
    }

    // Cart Table
    public long insertCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.CART_NAME, cart.getCart_name());
        contentValues.put(Util.CART_QUANTITY, cart.getCart_quantity());
        contentValues.put(Util.CART_PRICE, cart.getCart_price());
        contentValues.put(Util.CART_TOTAL_PRICE, cart.getTotal_price());
        long newRowId = db.insert(Util.TABLE_NAME_CART, null, contentValues);
        db.close();
        return newRowId;
    }

    public List<Cart> fetchAllCart() {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = " SELECT * FROM " + Util.TABLE_NAME_CART;
        Cursor cursor = db.rawQuery(selectAll, null);
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setCart_id(cursor.getInt(cursor.getColumnIndex(Util.CART_ID)));
                cart.setCart_name(cursor.getString(cursor.getColumnIndex(Util.CART_NAME)));
                cart.setCart_quantity(cursor.getInt(cursor.getColumnIndex(Util.CART_QUANTITY)));
                cart.setCart_price(cursor.getInt(cursor.getColumnIndex(Util.CART_PRICE)));
                cart.setTotal_price(cursor.getInt(cursor.getColumnIndex(Util.CART_TOTAL_PRICE)));
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        return cartList;
    }

    public int deleteCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Util.TABLE_NAME_CART, Util.CART_ID + "=?", new String[]{String.valueOf(cart.getCart_id())});
    }


    // Location table operations
    public long insertLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.LOCATION_NAME, location.getName());
        contentValues.put(Util.LATITUDE, location.getLatitude());
        contentValues.put(Util.LONGITUDE, location.getLongitude());
        long newRowId = db.insert(Util.TABLE_NAME_LOCATION, null, contentValues);
        db.close();
        return newRowId;
    }

    public int fetchLocation(String name, String latitude, String longitude) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME_LOCATION, new String[]{Util.LOCATION_ID}, Util.LOCATION_NAME + "=? and " + Util.LATITUDE + "=? and " + Util.LONGITUDE + "=?",
                new String[]{name, latitude, longitude}, null, null, null);

        if (cursor.moveToFirst()) {
            db.close();
            return cursor.getInt(cursor.getColumnIndex(Util.LOCATION_ID));
        } else {
            db.close();
            return -1;
        }
    }

    public List<Location> fetchAllLocation() {
        List<Location> locationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAll = " SELECT * FROM " + Util.TABLE_NAME_LOCATION;
        Cursor cursor = db.rawQuery(selectAll, null);

        if (cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(cursor.getInt(cursor.getColumnIndex(Util.LOCATION_ID)));
                location.setName(cursor.getString(cursor.getColumnIndex(Util.LOCATION_NAME)));
                location.setLatitude(cursor.getString(cursor.getColumnIndex(Util.LATITUDE)));
                location.setLongitude(cursor.getString(cursor.getColumnIndex(Util.LONGITUDE)));
                locationList.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return locationList;
    }
}
