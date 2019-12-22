package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.ActivitiesClass;
import com.example.arunavigator.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditActActivity extends AppCompatActivity {
    private ImageView img;
    private Button btn_choose_img,btn_edit,btn_delete,btn_choose_position;
    private TextInputEditText edit_name,edit_des,edit_lat,edit_long,edit_place_name;
    private Context mContext;
    private int Image_Request_Code;
    private Uri uri;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_act);

        init();
        Picasso.with(mContext).load(getIntent().getStringExtra("pic")).fit().centerCrop().into(img);
        edit_name.setText(getIntent().getStringExtra("name"));
        edit_des.setText(getIntent().getStringExtra("des"));
        edit_lat.setText(getIntent().getStringExtra("lat"));
        edit_long.setText(getIntent().getStringExtra("long"));
        edit_place_name.setText(getIntent().getStringExtra("place_name"));

        btn_choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "โปรดเลือกรุปภาพ"), Image_Request_Code);
            }
        });
        btn_choose_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(mContext, ChoosePositionActivity.class);
                startActivityForResult(intent,1234);
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(EditActActivity.this);
                builder.setCancelable(false);
                builder.setMessage("กรุณาตรวจสอบข้อมูลก่อนส่ง");
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "ลบแล้ว", Toast.LENGTH_SHORT).show();
                        deleteLocation(getIntent().getStringExtra("id"));
                        startActivity(new Intent(mContext,ManageAcActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == Image_Request_Code && data != null && data.getData() != null) {
                uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    img.setImageBitmap(bitmap);
                    btn_choose_img.setText("รูปที่เลือก");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == 1234){
                edit_lat.setText(data.getStringExtra("lat"));
                edit_long.setText(data.getStringExtra("mlong"));
            }
        }
    }

    private void init(){
        img = findViewById(R.id.edit_act_img);
        btn_choose_img = findViewById(R.id.edit_act_btn_choose_img);
        btn_edit = findViewById(R.id.edit_act_btn_edit);
        btn_delete = findViewById(R.id.edit_act_btn_delete);
        btn_choose_position = findViewById(R.id.edit_act_btn_choose_position);
        edit_name = findViewById(R.id.edit_act_name);
        edit_des = findViewById(R.id.edit_act_des);
        edit_lat = findViewById(R.id.edit_act_lat);
        edit_long = findViewById(R.id.edit_act_long);
        edit_place_name = findViewById(R.id.edit_place_name);
        mContext = getApplicationContext();
        Image_Request_Code = 7;
        Database_Path = "Activity";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }
    private String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }
    private boolean editAct(
            String id,
            String name,
            String des,
            String image,
            String place_name,
            String lat,
            String longg)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activity").child(id);
        ActivitiesClass activitiesClass = new ActivitiesClass();
        activitiesClass.setAd_id(id);
        activitiesClass.setAc_name(name);
        activitiesClass.setAc_des(des);
        activitiesClass.setAc_image(image);
        activitiesClass.setAc_place_name(place_name);
        activitiesClass.setLat(lat);
        activitiesClass.setMlong(longg);
        databaseReference.setValue(activitiesClass);
        return true;
    }
    private void send(){
        if (uri == null) {
            Toast.makeText(getApplicationContext(), "แก้ไขแล้ว", Toast.LENGTH_SHORT).show();
            editAct(
                    getIntent().getStringExtra("id"),
                    edit_name.getText().toString(),
                    edit_des.getText().toString(),
                    getIntent().getStringExtra("pic"),
                    edit_place_name.getText().toString(),
                    edit_lat.getText().toString(),
                    edit_long.getText().toString()
            );
            this.finishAffinity();
            startActivity(new Intent(mContext,ManageAcActivity.class));
        }
        else {
            final ProgressDialog pssss = new ProgressDialog(mContext);
            pssss.setTitle("กำลังแก้ไข......");
            pssss.show();
            FirebaseStorage storage;
            StorageReference storageReference;
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("Activity_image/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pssss.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getApplicationContext(), "แก้ไขแล้ว", Toast.LENGTH_SHORT).show();
                                    editAct(
                                            getIntent().getStringExtra("id"),
                                            edit_name.getText().toString(),
                                            edit_des.getText().toString(),
                                            uri.toString(),
                                            edit_place_name.getText().toString(),
                                            edit_lat.getText().toString(),
                                            edit_long.getText().toString()
                                    );
                                    startActivity(new Intent(mContext,ManageAcActivity.class));
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pssss.dismiss();
                    Toast.makeText(getApplicationContext(), "ล้มเหลว", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progess  = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    pssss.setMessage("กำลังแก้ไข "+(int)progess+"%");
                }
            });
        }
    }
    private void deleteLocation(String id){
        DatabaseReference drefLocation = FirebaseDatabase.getInstance().getReference("Activity").child(id);
        drefLocation.removeValue();
    }

}
