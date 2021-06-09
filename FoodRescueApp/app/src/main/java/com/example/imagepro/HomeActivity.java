package com.example.imagepro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imagepro.data.DatabaseHelper;
import com.example.imagepro.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements FoodViewAdapter.OnRowClickListener {
    DatabaseHelper db;
    List<Food> foodList = new ArrayList<>();
    TextView headerTextView;
    ImageView chatBotImageView;

    RecyclerView foodRecyclerView;
    FoodViewAdapter foodViewAdapter;
    RecyclerView.LayoutManager layoutManagerFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chatBotImageView = findViewById(R.id.chatBotImageView);

        chatBotImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent splashIntent = new Intent(HomeActivity.this, SplashActivity.class);
                startActivity(splashIntent);
            }
        });


        headerTextView = findViewById(R.id.headerTextView);
        db = new DatabaseHelper(this);
        foodList = db.fetchAllFood();

        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        foodViewAdapter = new FoodViewAdapter(foodList, HomeActivity.this, this);
        foodRecyclerView.setAdapter(foodViewAdapter);
        layoutManagerFood = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        foodRecyclerView.setLayoutManager(layoutManagerFood);

        FloatingActionButton addFabButton = findViewById(R.id.addFabButton);
        addFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(HomeActivity.this, AddFoodActivity.class);
                startActivityForResult(addIntent, 1);
            }
        });
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
                headerTextView.setText("Discover free food");
                db = new DatabaseHelper(HomeActivity.this);
                foodList = db.fetchAllFood();
                RecyclerView foodRecyclerView = findViewById(R.id.foodRecyclerView);
                FoodViewAdapter foodViewAdapter = new FoodViewAdapter(foodList, HomeActivity.this, HomeActivity.this);
                foodRecyclerView.setAdapter(foodViewAdapter);
                RecyclerView.LayoutManager layoutManagerFood = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                foodRecyclerView.setLayoutManager(layoutManagerFood);
                return true;

            case R.id.mylist:
                headerTextView.setText("My List");
                db = new DatabaseHelper(HomeActivity.this);
                // 另一种尝试
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                int user_id = sharedPref.getInt("CURRENT_USER", -1);
                // int user_id = getIntent().getIntExtra("CURRENT_USER", 0);
                foodList = db.fetchAllFood(user_id);
                foodRecyclerView = findViewById(R.id.foodRecyclerView);
                foodViewAdapter = new FoodViewAdapter(foodList, HomeActivity.this, HomeActivity.this);
                foodRecyclerView.setAdapter(foodViewAdapter);
                layoutManagerFood = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
                foodRecyclerView.setLayoutManager(layoutManagerFood);
                return true;

            case R.id.cart:
                Intent cartIntent = new Intent(HomeActivity.this, com.example.imagepro.CartActivity.class);
                startActivity(cartIntent);
                return true;

            case R.id.logout:
                // FirebaseAuth.getInstance().signOut();
                // change this code beacuse your app will crash
                startActivity(new Intent(HomeActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

            case R.id.detection:
                Intent detectionIntent = new Intent(HomeActivity.this, DetectorActivity.class);
                startActivity(detectionIntent);
                return true;

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, HomeActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    public void onItemClick(int position) {
        Intent detailIntent = new Intent(HomeActivity.this, DetailActivity.class);
        detailIntent.putExtra("position", position);
        startActivityForResult(detailIntent, 22);
    }

    public void onShareButtonClick(int position) {
        Food selectedFood = foodList.get(position);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Welcome to Food Rescue App " + selectedFood.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Come and rescue this food before it goes to waste! "
                + "\n\n" + selectedFood.getTitle()
                + "\n\n" + selectedFood.getDescription()
                + "\n\nDownload Food Rescue App to see details!");
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}