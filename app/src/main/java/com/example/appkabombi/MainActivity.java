package com.example.appkabombi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button captureImage,recognizeText;
    private ImageView photo;
    private TextView resultText;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        captureImage = findViewById(R.id.capture_image);
        recognizeText = findViewById(R.id.detect_text_image);
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

        recognizeText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                detectTextFromImage();
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

    private void detectTextFromImage(){
        final FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error: "+e.getMessage(),Toast.LENGTH_LONG);
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText){
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
        if (((List) blockList).size() == 0){
            Toast.makeText(this,"No Text Found in image.",Toast.LENGTH_SHORT);
        }else{
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()){
                String text = block.getText();
                resultText.setText(text);
            }
        }

    }
}
