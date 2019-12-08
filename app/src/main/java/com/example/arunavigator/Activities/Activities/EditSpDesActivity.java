package com.example.arunavigator.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.Adapter.ManageSpDesAdapter;
import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.Activities.Activities.GetterSetter.SpacificDes;
import com.example.arunavigator.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSpDesActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_edit,btn_delete;
    private TextInputEditText edit_des;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sp_des);

        init();
        edit_des.setText(getIntent().getStringExtra("des"));
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_des.getText().toString().isEmpty()){
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
                AlertDialog.Builder builder = new AlertDialog.Builder(EditSpDesActivity.this);
                builder.setCancelable(false);
                builder.setMessage("กรุณาตรวจสอบข้อมูลก่อนส่ง");
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "ลบแล้ว", Toast.LENGTH_SHORT).show();
                        delete();
                        startActivity(new Intent(mContext,SpacificDesActivity.class));
                    }
                });
                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }



    private void init(){
        mContext = getApplicationContext();
        btn_edit = findViewById(R.id.edit_sp_des_btn_edit);
        btn_delete = findViewById(R.id.edit_sp_des_btn_delete);
        edit_des = findViewById(R.id.edit_sp_des_edit_des);
        Database_Path = "SpacificDes";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }
    private void delete(){
        DatabaseReference drefLocation = FirebaseDatabase.getInstance().getReference("SpacificDes").child(getIntent().getStringExtra("id"));
        drefLocation.removeValue();
    }
    private void edit(){
        if(getIntent().getStringExtra("id").isEmpty()){
            Toast.makeText(mContext, "ไม่สามารถส่งข้อมูลได้", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(mContext,SpacificDesActivity.class));
        }
        else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SpacificDes").child(getIntent().getStringExtra("id"));
            SpacificDes spacificDes = new SpacificDes();
            spacificDes.setSp_id(getIntent().getStringExtra("id"));
            spacificDes.setLocation_id(getIntent().getStringExtra("location_id"));
            spacificDes.setSp_des(edit_des.getText().toString());
            databaseReference.setValue(spacificDes);
            startActivity(new Intent(mContext, SpacificDesActivity.class));
        }
    }
}
