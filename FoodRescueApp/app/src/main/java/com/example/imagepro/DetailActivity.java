package com.example.imagepro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagepro.data.DatabaseHelper;
import com.example.imagepro.model.Cart;
import com.example.imagepro.model.Food;
import com.example.imagepro.model.Location;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    DatabaseHelper db;
    List<Food> foodList = new ArrayList<>();
    List<Location> locationList = new ArrayList<>();

    private ImageView foodFragmentImageView, detailPayPalimageView;
    private TextView foodFragmentTitleTextView, foodFragmentDescriptionTextView,
            foodFragmentPickUpTimesTextView, foodFragmentQuantityTextView,
            foodFragmentLocationTextView, foodFragmentDateTextView, foodFragmentPriceTextView;
    private Button addToCartButton;

    public static final int PAYPAL_REQUEST_CODE = 30;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalClientIDConfigClass.PAYPAL_CLIENT_ID);

    String amount = "";
    private int index;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        foodFragmentImageView = findViewById(R.id.foodFragmentImageView);
        detailPayPalimageView = findViewById(R.id.detailPayPalimageView);

        foodFragmentTitleTextView = findViewById(R.id.foodFragmentTitleTextView);
        foodFragmentDescriptionTextView = findViewById(R.id.foodFragmentDescriptionTextView);
        foodFragmentPickUpTimesTextView = findViewById(R.id.foodFragmentPickUpTimesTextView);
        foodFragmentQuantityTextView = findViewById(R.id.foodFragmentQuantityTextView);
        foodFragmentLocationTextView = findViewById(R.id.foodFragmentLocationTextView);
        foodFragmentPriceTextView = findViewById(R.id.foodFragmentPriceTextView);

        foodFragmentDateTextView = findViewById(R.id.foodFragmentDateTextView);

        addToCartButton = findViewById(R.id.addToCartButton);

        db = new DatabaseHelper(this);
        foodList = db.fetchAllFood();
        locationList = db.fetchAllLocation();

        // Start Paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        int currentFoodID = getIntent().getIntExtra("position", -1);

        String path = foodList.get(currentFoodID).getImage();
        if (path != null) {
            File imgFile = new File(path);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                foodFragmentImageView.setImageBitmap(myBitmap);
            } else {
                foodFragmentImageView.setImageResource(R.drawable.image);
            }
        } else {
            foodFragmentImageView.setImageResource(R.drawable.image);
        }

        foodFragmentTitleTextView.setText(foodList.get(currentFoodID).getTitle());
        foodFragmentDescriptionTextView.setText(foodList.get(currentFoodID).getDescription());
        foodFragmentPickUpTimesTextView.setText(foodList.get(currentFoodID).getPickUpTimes());
        foodFragmentQuantityTextView.setText(String.valueOf(foodList.get(currentFoodID).getQuantity()));
        foodFragmentLocationTextView.setText(foodList.get(currentFoodID).getLocation());
        foodFragmentDateTextView.setText(String.valueOf(foodList.get(currentFoodID).getDate()));
        foodFragmentPriceTextView.setText(String.valueOf(foodList.get(currentFoodID).getPrice()));

        foodFragmentLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < locationList.size(); i++) {
                    if (locationList.get(i).getName().equals(foodFragmentLocationTextView.getText().toString())) {
                        index = i;
                    }
                }

                Intent intent = new Intent(DetailActivity.this, MapActivity.class);
                intent.putExtra("Latitude", Double.parseDouble(locationList.get(index).getLatitude()));
                intent.putExtra("Longitude", Double.parseDouble(locationList.get(index).getLongitude()));
                intent.putExtra("Name", foodFragmentLocationTextView.getText().toString());
                startActivity(intent);
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart newCart = new Cart();
                newCart.setCart_name(foodFragmentTitleTextView.getText().toString());
                newCart.setCart_quantity(Integer.parseInt(foodFragmentQuantityTextView.getText().toString()));
                newCart.setCart_price(Integer.parseInt(foodFragmentPriceTextView.getText().toString()));
                newCart.setTotal_price(Integer.parseInt(foodFragmentQuantityTextView.getText().toString()) * Integer.parseInt(foodFragmentPriceTextView.getText().toString()));
                long result = db.insertCart(newCart);
                setResult(RESULT_OK, null);
                finish();
                Toast.makeText(DetailActivity.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();

                // Jump to the CartActivity
//                Intent cartIntent = new Intent(DetailActivity.this, CartActivity.class);
//                startActivity(cartIntent);
            }
        });

        // Paypal Button
        detailPayPalimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    private void processPayment() {
        amount = String.valueOf(Integer.parseInt(foodFragmentQuantityTextView.getText().toString()) * Integer.parseInt(foodFragmentPriceTextView.getText().toString()));
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),
                "AUD",
                "Pay for " + foodFragmentTitleTextView.getText().toString(),
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Payment Made Successfully", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }
}