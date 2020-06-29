package com.ktechsoft.shareall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class folderView extends AppCompatActivity {

    int int_position;
    private GridView gridView;
    GridViewAdapter adapter;
    ArrayList<Model_images> al_images = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);
        gridView = (GridView)findViewById(R.id.gv_folder);
        int_position = getIntent().getIntExtra("value", 0);
        al_images = (ArrayList<Model_images>) getIntent().getSerializableExtra("List");
        adapter = new GridViewAdapter(this,al_images,int_position);
        gridView.setAdapter(adapter);
    }
}
