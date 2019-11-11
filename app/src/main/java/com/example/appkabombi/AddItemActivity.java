package com.example.appkabombi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    private BottomAppBar bottomAppBar;
    private FloatingActionButton captureImage;
    private FloatingActionButton getImageGallery;
    private FloatingActionButton saveInsert;
    private EditText inputLabel;
    private EditText inputDetail;
    private ImageView photo;

    DatabaseHelper sqLiteDatabase = new DatabaseHelper(this);
    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        captureImage = findViewById(R.id.floatCameraBtn);
        getImageGallery = findViewById(R.id.floatGaleryBtn);
        saveInsert = findViewById(R.id.floatConfirmBtn);
        inputLabel = findViewById(R.id.labelInput);
        inputDetail = findViewById(R.id.detailInput);
        photo = findViewById(R.id.image_View);

        saveInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        setSupportActionBar(bottomAppBar);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraIntent();
            }
        });

        try {
            Bundle oldPage = getIntent().getExtras();
            Integer idItem = oldPage.getInt("idDB");
            if (idItem != null){
//      ID INTEGER PRIMARY KEY AUTOINCREMENT,LABEL TEXT,DETAIL TEXT,DATE INTEGER,IMAGEPATH TEXT
                System.out.println("skuskaaaaa");
                System.out.println(idItem.intValue());
                String uriPath = "";
                String label = "";
                String detail = "";
                Cursor c = sqLiteDatabase.getRecord(idItem.intValue());
                while (c.moveToNext()){
                    uriPath = c.getString(4);
                    label = c.getString(1);
                    detail = c.getString(2);
                }
                System.out.println("uriPath: "+uriPath);
                System.out.println("label: "+label);
                System.out.println("detail: "+detail);
                Uri uri = Uri.parse(uriPath);
                inputLabel.setText(label);
                inputDetail.setText(detail);
                photo.setImageURI(uri);
            }
        }catch (Exception e){
            e.getMessage();
        }

    }


    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() +".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                photo.setImageURI(Uri.parse(imageFilePath));
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }

    private void saveData(){
        String label = String.valueOf(inputLabel.getText());
        String detail = String.valueOf(inputDetail.getText());

        if (inputLabel.getText().equals("") || inputDetail.getText().equals("")){
            Toast.makeText(AddItemActivity.this,"Inputs can't be empty",Toast.LENGTH_SHORT).show();
        }else if(imageFilePath.equals("")){
            Toast.makeText(AddItemActivity.this,"Pick some image",Toast.LENGTH_SHORT).show();
        }else {
            sqLiteDatabase = new DatabaseHelper(this);
            sqLiteDatabase.insertData(label,detail,1,imageFilePath);
            Intent back = new Intent(AddItemActivity.this,MainActivity.class);
            startActivity(back);
        }
    }
}
