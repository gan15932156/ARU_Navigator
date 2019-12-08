package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.Toilet;
import com.example.arunavigator.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditToiletActivity extends AppCompatActivity {
    private ImageView img;
    private Button btn_choose_img,btn_edit,btn_delete;
    private Context mContext;
    private int Image_Request_Code;
    private Uri uri;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_toilet);

        init();
        Picasso.with(mContext).load(getIntent().getStringExtra("pic")).fit().centerCrop().into(img);

        btn_choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "โปรดเลือกรุปภาพ"), Image_Request_Code);
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


                AlertDialog.Builder builder = new AlertDialog.Builder(EditToiletActivity.this);
                builder.setCancelable(false);
                builder.setMessage("กรุณาตรวจสอบข้อมูลก่อนส่ง");
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "ลบแล้ว", Toast.LENGTH_SHORT).show();
                        deleteLocation(getIntent().getStringExtra("toilet_id"));
                        startActivity(new Intent(mContext,ManageToiletActivity.class));
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
    private String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }
    private void send(){
        if (uri == null) {
            Toast.makeText(getApplicationContext(), "แก้ไขแล้ว", Toast.LENGTH_SHORT).show();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Toilet").child(getIntent().getStringExtra("id"));

            Toilet toilet = new Toilet();
            toilet.setFloor_id(getIntent().getStringExtra("floor_id"));
            toilet.setToilet_id(getIntent().getStringExtra("toilet_id"));
            toilet.setToilet_map_pic(getIntent().getStringExtra("pic"));
            toilet.setToilet_name(getIntent().getStringExtra("toilet_name"));
            toilet.setToilet_type(getIntent().getStringExtra("type"));
            databaseReference.setValue(toilet);

            this.finishAffinity();
            startActivity(new Intent(mContext,ManageToiletActivity.class));
        }
        else {
            final ProgressDialog pssss = new ProgressDialog(mContext);
            pssss.setTitle("กำลังแก้ไข......");
            pssss.show();
            FirebaseStorage storage;
            StorageReference storageReference;
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("Toilet_image/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pssss.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(getApplicationContext(), "แก้ไขแล้ว", Toast.LENGTH_SHORT).show();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Toilet").child(getIntent().getStringExtra("id"));

                                    Toilet toilet = new Toilet();
                                    toilet.setFloor_id(getIntent().getStringExtra("floor_id"));
                                    toilet.setToilet_id(getIntent().getStringExtra("toilet_id"));
                                    toilet.setToilet_map_pic(uri.toString());
                                    toilet.setToilet_name(getIntent().getStringExtra("toilet_name"));
                                    toilet.setToilet_type(getIntent().getStringExtra("type"));
                                    databaseReference.setValue(toilet);
                                    startActivity(new Intent(mContext,ManageToiletActivity.class));
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
        DatabaseReference drefLocation = FirebaseDatabase.getInstance().getReference("Toilet").child(id);
        drefLocation.removeValue();
    }

    private void init(){
        img = findViewById(R.id.edit_toilet_img);
        btn_choose_img = findViewById(R.id.edit_toilet_btn_choose_img);
        btn_edit = findViewById(R.id.edit_toilet_btn_edit);
        btn_delete = findViewById(R.id.edit_toilet_btn_delete);
        mContext = getApplicationContext();
        Image_Request_Code = 7;
        Database_Path = "Toilet";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }
}
