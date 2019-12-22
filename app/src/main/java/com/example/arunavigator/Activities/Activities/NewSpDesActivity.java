package com.example.arunavigator.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.SpacificDes;
import com.example.arunavigator.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewSpDesActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_send,btn_back;
    private TextInputEditText edit_name,edit_des;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sp_des);
        init();
        edit_name.setText(getIntent().getStringExtra("name"));
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_name.getText().toString().isEmpty() &&
                        !edit_des.getText().toString().isEmpty()){
                    insert();
                }
                else{
                    Toast.makeText(mContext, "กรุณากรอกข้อมูลให้สมบูรณ์", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,SpacificDesActivity.class));
                finishAffinity();
            }
        });
    }
    private void init(){
        mContext = NewSpDesActivity.this;
        btn_send = findViewById(R.id.new_sp_des_btn_send);
        btn_back = findViewById(R.id.new_sp_des_btn_back);
        edit_name = findViewById(R.id.new_sp_des_edit_name);
        edit_des = findViewById(R.id.new_sp_des_edit_des);
        Database_Path = "SpacificDes";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }

    private void insert(){
        if(getIntent().getStringExtra("id").isEmpty()){
            Toast.makeText(mContext, "ไม่สามารถส่งข้อมูลได้", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,SpacificDesActivity.class));
        }
        else{
            String sp_id = databaseReference.push().getKey();
            SpacificDes spacificDes = new SpacificDes(sp_id,getIntent().getStringExtra("id"),edit_des.getText().toString());
            databaseReference.child(sp_id).setValue(spacificDes);
            startActivity(new Intent(mContext, SpacificDesActivity.class));
        }
    }
}
