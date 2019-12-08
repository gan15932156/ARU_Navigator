package com.example.arunavigator.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.ActivitiesClass;
import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditFloorActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_edit,btn_delete;
    private TextInputEditText edit_number;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_floor);


        init();
        edit_number.setText(getIntent().getStringExtra("floor_number"));
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_number.getText().toString().isEmpty()){
                    edit();
                }
                else{
                    Toast.makeText(mContext, "กรุณากรอกข้อมูลให้สมบูรณ์", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }


    private void init(){
        mContext = getApplicationContext();
        btn_edit = findViewById(R.id.edit_floor_btn_edit);
        btn_delete = findViewById(R.id.edit_floor_btn_delete);
        edit_number = findViewById(R.id.edit_floor_edit_number);
        Database_Path = "Floor";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }
    private void delete(){
        DatabaseReference drefLocation = FirebaseDatabase.getInstance().getReference("Floor").child(getIntent().getStringExtra("floor_id"));
        drefLocation.removeValue();
        startActivity(new Intent(mContext, ManageFloorActivity.class));
    }
    private void edit(){
        if(getIntent().getStringExtra("floor_id").isEmpty()){
            Toast.makeText(mContext, "ไม่สามารถส่งข้อมูลได้", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,ManageFloorActivity.class));
        }
        else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Floor").child(getIntent().getStringExtra("floor_id"));
            Floor floor = new Floor();
            floor.setFloor_id(getIntent().getStringExtra("floor_id"));
            floor.setFloor_number(edit_number.getText().toString());
            floor.setLocation_id(getIntent().getStringExtra("location_id"));

            databaseReference.setValue(floor);
            startActivity(new Intent(mContext, ManageFloorActivity.class));
        }
    }
}
