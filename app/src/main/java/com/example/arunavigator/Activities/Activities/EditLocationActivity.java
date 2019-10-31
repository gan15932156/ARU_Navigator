package com.example.arunavigator.Activities.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.arunavigator.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class EditLocationActivity extends AppCompatActivity {
    private ImageView img;
    private Button btn_choose_img,btn_send,btn_choose_position;
    private TextInputEditText edit_name,edit_des,edit_lat,edit_long;
    private Spinner spn_type;
    private Context mContext;
    private int Image_Request_Code;
    private Uri uri;
    private String Database_Path;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        init();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sp_type,android.R.layout.simple_spinner_item);
        spn_type.setAdapter(adapter);

        Picasso.with(mContext).load(getIntent().getStringExtra("pic")).fit().centerCrop().into(img);
        edit_name.setText(getIntent().getStringExtra("name"));
        edit_des.setText(getIntent().getStringExtra("des"));
        edit_lat.setText(getIntent().getStringExtra("lat"));
        edit_long.setText(getIntent().getStringExtra("long"));
        String compareValue = getIntent().getStringExtra("type");
        if (compareValue != null) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spn_type.setSelection(spinnerPosition);
        }
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
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void init(){
        img = findViewById(R.id.edit_location_img);
        btn_choose_img = findViewById(R.id.edit_location_btn_choose_img);
        btn_send = findViewById(R.id.edit_location_btn_send);
        btn_choose_position = findViewById(R.id.edit_location_btn_choose_position);
        edit_name = findViewById(R.id.edit_location_name);
        edit_des = findViewById(R.id.edit_location_des);
        edit_lat = findViewById(R.id.edit_location_lat);
        edit_long = findViewById(R.id.edit_location_long);
        spn_type = findViewById(R.id.edit_location_spn_type);
        mContext = getApplicationContext();
        Image_Request_Code = 7;
        Database_Path = "Building";
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
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
