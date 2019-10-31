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

import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
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

public class NewLocationActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView img;
    private Button btn_send,btn_choose_img,btn_choose_position;
    private TextInputEditText edit_name,edit_des,edit_lat,edit_long;
    private Spinner spn_type;
    private int Image_Request_Code;
    private Uri uri;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);
        init();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        btn_choose_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(mContext, ChoosePositionActivity.class);
                startActivityForResult(intent,1234);
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
    private void init(){
        mContext = getApplicationContext();
        img = findViewById(R.id.new_location_img_location);
        btn_send = findViewById(R.id.new_location_btn_send);
        btn_choose_img = findViewById(R.id.new_location_btn_choose_img);
        btn_choose_position = findViewById(R.id.new_location_btn_choose_position);
        edit_name = findViewById(R.id.new_location_edit_location_name);
        edit_des = findViewById(R.id.new_location_edit_location_des);
        edit_lat = findViewById(R.id.new_location_edit_location_lat);
        edit_long = findViewById(R.id.new_location_edit_location_long);
        spn_type = findViewById(R.id.new_location_spn_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sp_type,android.R.layout.simple_spinner_item);
        spn_type.setAdapter(adapter);
        Image_Request_Code = 7;
        Database_Path = "Building";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
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
            final StorageReference ref = storageReference.child("Building_image/" + System.currentTimeMillis() + "." + GetFileExtension(uri));
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ps.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    @SuppressWarnings("VisibleForTests")
                                    String location_id = databaseReference.push().getKey();
                                    Location location = new Location(edit_name.getText().toString(),edit_des.getText().toString(),uri.toString(),
                                            spn_type.getSelectedItem().toString(),location_id,edit_lat.getText().toString(),edit_long.getText().toString());
                                    databaseReference.child(location_id).setValue(location);
                                    startActivity(new Intent(mContext, NewLocationActivity.class));
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
            else if(requestCode == 1234){
                edit_lat.setText(data.getStringExtra("lat"));
                edit_long.setText(data.getStringExtra("mlong"));
            }
        }
    }

    private String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;
    }
}
