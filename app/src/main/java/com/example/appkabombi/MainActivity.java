package com.example.appkabombi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton addingPicturesFloatBtn;

    ArrayList<Integer> listPics;
    GridView galery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        addingPicturesFloatBtn = findViewById(R.id.floatAddBtn);
        setSupportActionBar(bottomAppBar);

        galery = (GridView)findViewById(R.id.image_Grid);
        listPics = getData();
        galery.setAdapter(new GridAdapter());
        galery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,AddItemActivity.class);
                startActivity(intent);
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
                imageView.setImageResource(listPics.get(position).intValue());
            }
            return convertView;
        }

    }

    private ArrayList<Integer> getData() {

        ArrayList<Integer> list = new ArrayList<>();

        for (int a=0; a<30; a++){
            Integer imageR;
            if (a%2==0){
                imageR = R.drawable.ic_launcher_foreground;
            }else{
                imageR = R.drawable.ic_launcher_background;
            }
            list.add(imageR);
        }

        return list;
    }


}
