package com.example.imagepro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.imagepro.data.DatabaseHelper;
import com.example.imagepro.ml.MobilenetV110224Quant;
import com.example.imagepro.model.Food;
import com.example.imagepro.model.Location;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddFoodActivity extends AppCompatActivity {
    DatabaseHelper db;
    String imageLocation;
    ImageView foodImageView;

    List<Location> locationList = new ArrayList<>();

    private Bitmap img;
    String[] labels;
    String sDate = "";

    EditText locationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        foodImageView = findViewById(R.id.foodImageView);
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText descriptionEditText = findViewById(R.id.descriptionEditText);
        EditText priceEditText = findViewById(R.id.priceEditText);
        EditText pickupTimesEditText = findViewById(R.id.pickuptimesEditText);
        EditText quantityEditText = findViewById(R.id.quantityEditText);
        locationEditText = findViewById(R.id.locationEditText);
        CalendarView calendarView = findViewById(R.id.calendarView);
        Button addSaveButton = findViewById(R.id.addSaveButton);
        Button addImageButton = findViewById(R.id.addImageButton);
        db = new DatabaseHelper(this);
        locationList = db.fetchAllLocation();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                sDate = sdf.format(calendar.getTime());
                System.out.println(sDate);
            }
        });

        addSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditText.getText().toString().equals("") || descriptionEditText.getText().toString().equals("") || priceEditText.getText().toString().equals("") ||
                        pickupTimesEditText.getText().toString().equals("") || quantityEditText.getText().toString().equals("") ||
                        locationEditText.getText().toString().equals("")) {
                    Toast.makeText(AddFoodActivity.this, "Please enter all information!", Toast.LENGTH_SHORT).show();
                } else {
                    Food newFood = new Food();
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AddFoodActivity.this);
                    int user_id = sharedPref.getInt("CURRENT_USER", 0);
                    newFood.setUser_id(user_id);
                    newFood.setImage(imageLocation);
                    newFood.setTitle(titleEditText.getText().toString());
                    newFood.setDescription(descriptionEditText.getText().toString());
                    newFood.setPrice(Integer.parseInt(priceEditText.getText().toString()));
                    newFood.setDate(sDate);
                    newFood.setPickUpTimes(pickupTimesEditText.getText().toString());
                    newFood.setQuantity(Integer.parseInt(quantityEditText.getText().toString()));
                    newFood.setLocation(locationEditText.getText().toString());
                    long result = db.insertFood(newFood);
                    setResult(RESULT_OK, null);
                    finish();
                }
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(AddFoodActivity.this, AddMapActivity.class);
                startActivityForResult(mapIntent, 1);
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddFoodActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                            AddFoodActivity.this,
                            new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            },
                            101);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 2);
                }
            }
        });

        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = Bitmap.createScaledBitmap(img, 224, 224, true);
                try {
                    InputStream inputStream = getAssets().open("labels.txt");
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];
                    inputStream.read(buffer);
                    inputStream.close();
                    labels = new String(buffer).split("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    MobilenetV110224Quant model = MobilenetV110224Quant.newInstance(getApplicationContext());
                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                    TensorImage tensorImage = new TensorImage(DataType.UINT8);
                    tensorImage.load(img);
                    ByteBuffer byteBuffer = tensorImage.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);
                    // Runs model inference and gets result.
                    MobilenetV110224Quant.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    int max = getMax(outputFeature0.getFloatArray());
                    titleEditText.setText(labels[max]);
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == 1 && resultCode == 3) {
            if (data != null) {
                String result = data.getStringExtra("name");
                locationEditText.setText(result);

                for (int i = 0; i < locationList.size(); i++) {
                    System.out.println(locationList.get(i).getName());
                    System.out.println(locationList.get(i).getLatitude());
                    System.out.println(locationList.get(i).getLongitude());
                }
            }
        }

        if (resultCode == RESULT_OK) {
            try {
                foodImageView.setImageURI(data.getData());
                Uri uri = data.getData();
                try {
                    img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageLocation = saveImage(this, selectedImage);
                File test = new File(imageLocation);
                Bitmap myBitmap = BitmapFactory.decodeFile(test.getAbsolutePath());
                foodImageView.setImageBitmap(myBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddFoodActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        } else {
//            Toast.makeText(com.android.app.examples.rescue.AddFoodActivity.this, "Please choose a image!", Toast.LENGTH_LONG).show();
        }
    }

    public final int getMax(float[] arr) {
        int ind = 0;
        float min = 0.0F;
        int i = 0;

        for (short var5 = 1000; i <= var5; ++i) {
            if (arr[i] > min) {
                min = arr[i];
                ind = i;
            }
        }
        return ind;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 2);
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImage(Context context, Bitmap image) {
        String savedImagePath = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/FoodRescue");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String savedMessage = "Saved";
        }
        return savedImagePath;
    }
}