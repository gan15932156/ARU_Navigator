package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.Toilet;
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

import java.io.IOException;

public class NewToiletActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView img;
    private Button btn_send, btn_choose_img;
    private TextInputEditText edit_name, edit_number;
    private Spinner spn_type;
    private int Image_Request_Code;
    private Uri uri;
    private String Database_Path;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_toilet);

        init();

        edit_name.setText(getIntent().getStringExtra("location_name"));
        edit_number.setText(getIntent().getStringExtra("floor_number"));
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit_name.getText().toString().isEmpty() &&
                        !edit_number.getText().toString().isEmpty()

                ){
                    insert();
                }
                else{
                    Toast.makeText(mContext, "กรุณากรอกข้อมูลให้สมบูรณ์", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "โปรดเลือกรุปภาพ"), Image_Request_Code);
            }
        });
    }

    private void init() {
        mContext = getApplicationContext();
        img = findViewById(R.id.new_toilet_img_toilet);
        btn_send = findViewById(R.id.new_toilet_btn_send);
        btn_choose_img = findViewById(R.id.new_toilet_btn_choose_img);
        edit_name = findViewById(R.id.new_toilet_edit_name);
        edit_number = findViewById(R.id.new_toilet_edit_number);
        spn_type = findViewById(R.id.new_toilet_spn_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spn_toilet_type, android.R.layout.simple_spinner_item);
        spn_type.setAdapter(adapter);
        Image_Request_Code = 7;
        Database_Path = "Toilet";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }
    private String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }
    private void insert(){
        if (uri != null) {
            final ProgressDialog ps = new ProgressDialog(this);
            ps.setTitle("กำลังอัพโหลด......");
            ps.show();
            FirebaseStorage storage;
            StorageReference storageReference;
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("Toilet_image/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ps.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    @SuppressWarnings("VisibleForTests")
                                    String toilet_id = databaseReference.push().getKey();

                                    String toilet_name = "ห้องน้ำ"+spn_type.getSelectedItem().toString()+"_ชั้น"+edit_number.getText().toString();
                                    Toilet toilet = new Toilet(
                                          toilet_id,
                                          getIntent().getStringExtra("floor_id"),
                                            toilet_name,
                                            uri.toString(),
                                            spn_type.getSelectedItem().toString()
                                    );
                                    databaseReference.child(toilet_id).setValue(toilet);


                                    startActivity(new Intent(mContext, ManageToiletActivity.class));
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ps.dismiss();
                    Toast.makeText(mContext, "ล้มแหลว", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progess  = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    ps.setMessage("สำเร็จ "+(int)progess+"%");
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "กรุณาเลือกรูป", Toast.LENGTH_LONG).show();
        }
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
        }
    }
}
