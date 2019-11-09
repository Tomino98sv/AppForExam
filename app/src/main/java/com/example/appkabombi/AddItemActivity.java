package com.example.appkabombi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    private Button captureImage;
    private ImageView photo;
    private TextView resultText;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        captureImage = findViewById(R.id.capture_image);
        photo = findViewById(R.id.image_View);
        resultText = findViewById(R.id.text_View);

        resultText.setText("result here");

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                resultText.setText("");
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
