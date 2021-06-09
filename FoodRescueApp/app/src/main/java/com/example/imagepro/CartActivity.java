package com.example.imagepro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagepro.data.DatabaseHelper;
import com.example.imagepro.model.Cart;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    CartViewAdapter cartViewAdapter;
    List<Cart> cartList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManagerFood;
    private TextView totalPriceTextView;
    private int total;
    ImageView cartPayPalImageView;
    DatabaseHelper db;

    public static final int PAYPAL_REQUEST_CODE = 30;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PaypalClientIDConfigClass.PAYPAL_CLIENT_ID);

    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        db = new DatabaseHelper(this);
        cartList = db.fetchAllCart();

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartViewAdapter = new CartViewAdapter(cartList, CartActivity.this);
        cartRecyclerView.setAdapter(cartViewAdapter);
        layoutManagerFood = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        cartRecyclerView.setLayoutManager(layoutManagerFood);

        total = 0;
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        for (int i = 0; i < cartList.size(); i++) {
            System.out.println(cartList.get(i).getCart_id());
            System.out.println(cartList.get(i).getTotal_price());
            total += cartList.get(i).getTotal_price();
        }
        totalPriceTextView.setText("$ " + String.valueOf(total));

        // Start Paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        cartPayPalImageView = findViewById(R.id.cartPayPalImageView);
        cartPayPalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    private void processPayment() {
        amount = String.valueOf(total);
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),
                "AUD",
                "Pay for cart food",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                return true;

            case R.id.logout:
                // FirebaseAuth.getInstance().signOut();
                // change this code beacuse your app will crash
                startActivity(new Intent(CartActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

            case R.id.detection:
                Intent detectionIntent = new Intent(CartActivity.this, DetectorActivity.class);
                startActivity(detectionIntent);
                return true;
        }
        return false;
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