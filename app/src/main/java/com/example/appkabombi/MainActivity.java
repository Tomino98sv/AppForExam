package com.example.appkabombi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton addingPicturesFloatBtn;

    ArrayList<PictureInfo> listPics;
    GridView galery;

    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteDatabase = openOrCreateDatabase("MemPicsApp", Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS PicturesRecords(PicsId INTEGER PRIMARY KEY AUTOINCREMENT,Label VARCHAR(255),Detail VARCHAR(255),PicsPath VARCHAR(255), DateTime INTEGER);");

        bottomAppBar = findViewById(R.id.bottomAppBar);
        galery = (GridView)findViewById(R.id.image_Grid);
        addingPicturesFloatBtn = findViewById(R.id.floatAddBtn);

        setSupportActionBar(bottomAppBar);

        getData();
        galery.setAdapter(new GridAdapter());
        galery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        addingPicturesFloatBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddItemActivity.class);
                startActivity(intent);
            }
        });
    }

    public class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listPics.size();
        }

        @Override
        public Object getItem(int position) {
            return listPics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.image_item,parent,false);
                ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
                imageView.setImageURI(Uri.parse(listPics.get(position).path));
            }
            return convertView;
        }

    }

    private void getData() {
        listPics = new ArrayList<>();
            Cursor c = sqLiteDatabase.rawQuery("Select * From PicturesRecords",null);
            if (c != null){
                while (c.moveToNext()){
                    PictureInfo picsInfo = new PictureInfo(
                            c.getString(4),
                            c.getString(2),
                            c.getInt(1)
                    );

                    listPics.add(picsInfo);
                }
            }
    }


}
