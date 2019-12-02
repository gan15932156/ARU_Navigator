package com.example.arunavigator.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.arunavigator.R;

public class IndexAdminActivity extends AppCompatActivity {
    private Button btn_location,btn_spacific_des,btn_ac,btn_floor,btn_toilet,btn_vertex,btn_edge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_admin);
        init();
        btn_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexAdminActivity.this,ManageAcActivity.class));
                finish();
            }
        });
        btn_edge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexAdminActivity.this,ManageLocationActivity.class));
                finish();
            }
        });
        btn_spacific_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_vertex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void init(){
        btn_ac = findViewById(R.id.index_admin_btn_manage_activity);
        btn_edge = findViewById(R.id.index_admin_btn_manage_edge);
        btn_floor = findViewById(R.id.index_admin_btn_manage_edge);
        btn_location = findViewById(R.id.index_admin_btn_manage_location);
        btn_spacific_des = findViewById(R.id.index_admin_btn_manage_spacific_des);
        btn_toilet = findViewById(R.id.index_admin_btn_manage_toilet);
        btn_vertex = findViewById(R.id.index_admin_btn_manage_vertex);
    }
}
