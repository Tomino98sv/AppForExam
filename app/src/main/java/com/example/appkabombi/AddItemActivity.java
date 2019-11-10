package com.example.appkabombi;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private Button captureImage;
    private Button getImageGallery;
    private Button saveInsert;
    private EditText inputLabel;
    private EditText inputDetail;
    private ImageView photo;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        sqLiteDatabase = openOrCreateDatabase("MemPicsApp", Context.MODE_PRIVATE,null);

        captureImage = findViewById(R.id.capture_image);
        getImageGallery = findViewById(R.id.get_image_fromGallery);
        saveInsert = findViewById(R.id.saveInsert);
        inputLabel = findViewById(R.id.labelInput);
        inputDetail = findViewById(R.id.detailInput);
        photo = findViewById(R.id.image_View);


        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //sending user to external activity to capture image
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) { //to handle the image data when focus returns to your activity.
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            //startActivityForResult pointing to onActivityResult method
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //picture capture here
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();           //geting data and use in imageBitmat
            imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
        }
    }
}
