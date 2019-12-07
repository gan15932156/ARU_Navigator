package com.example.arunavigator.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.ActivitiesClass;
import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewFloorActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_send;
    private TextInputEditText edit_name,edit_number;
    private Uri uri;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_floor);

        init();
        edit_name.setText(getIntent().getStringExtra("name"));
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_name.getText().toString().isEmpty() &&
                    !edit_number.getText().toString().isEmpty()){
                    insert();
                }
                else{
                    Toast.makeText(mContext, "กรุณากรอกข้อมูลให้สมบูรณ์", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void init(){
        mContext = getApplicationContext();
        btn_send = findViewById(R.id.new_floor_btn_send);
        edit_name = findViewById(R.id.new_floor_edit_name);
        edit_number = findViewById(R.id.new_floor_edit_number);
        Database_Path = "Floor";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }

    private void insert(){
        if(getIntent().getStringExtra("id").isEmpty()){
            Toast.makeText(mContext, "ไม่สามารถส่งข้อมูลได้", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,ManageFloorActivity.class));
        }
        else{
            String f_id = databaseReference.push().getKey();
            Floor floor = new Floor(f_id,getIntent().getStringExtra("id"),edit_number.getText().toString());
            databaseReference.child(f_id).setValue(floor);
            startActivity(new Intent(mContext, ManageFloorActivity.class));
        }
    }
}
