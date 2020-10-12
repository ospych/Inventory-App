package com.example.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.inventoryapp.EXTRA_TITLE";
    public static final String EXTRA_SUPPLIER = "com.example.inventoryapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_QUANTITY = "com.example.inventoryapp.EXTRA_QUANTITY";
    public static final String EXTRA_PRICE = "com.example.inventoryapp.EXTRA_PRICE";
    public static final String EXTRA_IMAGE = "com.example.inventoryapp.EXTRA_IMAGE";
    public static final String EXTRA_ID = "com.example.inventoryapp.EXTRA_ID";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    File storageDir;
    File image;
    String currentPhotoPath;
    Uri photoURI;


    private EditText title;
    private EditText supplier;
    private EditText quantity;
    private EditText price;
    private ImageView imageIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.edit_title);
        supplier = findViewById(R.id.edit_supplier);
        price = findViewById(R.id.edit_price);
        quantity = findViewById(R.id.edit_quantity);
        imageIV = findViewById(R.id.view_image);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            supplier.setText(intent.getStringExtra(EXTRA_SUPPLIER));
            price.setText(String.valueOf(intent.getDoubleExtra(EXTRA_PRICE, 1)));
            quantity.setText(String.valueOf(intent.getIntExtra(EXTRA_QUANTITY, 1)));
        } else {
            setTitle("Add Note");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                saveItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void saveItem() {
        String titleTV;
        String supplierTV;
        double priceTV;
        int quantityTV;

        if (title.getEditableText().toString().length() == 0 || supplier.getEditableText()
                .toString()
                .length() == 0) {
            Toast.makeText(this, "Please input title and description", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (price.getEditableText().toString().length() == 0) {
            Toast.makeText(this, "Please input title and description", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        titleTV = title.getText().toString();
        supplierTV = supplier.getText().toString();
        priceTV = Double.parseDouble(price.getText().toString());
        quantityTV = Integer.parseInt(quantity.getText().toString());
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String picture = photoURI.toString();



        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, titleTV);
        data.putExtra(EXTRA_SUPPLIER, supplierTV);
        data.putExtra(EXTRA_QUANTITY, quantityTV);
        data.putExtra(EXTRA_PRICE, priceTV);
        data.putExtra(EXTRA_IMAGE, picture);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this).load(image.getAbsolutePath()).into(imageIV);
        }
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.inventoryapp.fileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}