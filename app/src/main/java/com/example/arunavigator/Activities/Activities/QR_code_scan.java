package com.example.arunavigator.Activities.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR_code_scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    String resultText = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(QR_code_scan.this);
        setContentView(scannerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scannerView.setResultHandler(this);
            scannerView.startCamera();
        } else {
            ActivityCompat.requestPermissions(QR_code_scan.this, new
                    String[]{Manifest.permission.CAMERA}, 1024);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
    @Override
    public void handleResult(Result result) {
        resultText = result.getText(); /* Retrieving text from QR Code */
        Intent intent = new Intent(this,Show_acActivity.class);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(resultText);
            intent.putExtra("ac_des",jsonObject.getString("ac_des"));
            intent.putExtra("ac_id",jsonObject.getString("ac_id"));
            intent.putExtra("ac_name",jsonObject.getString("ac_name"));
            intent.putExtra("ac_place_name",jsonObject.getString("ac_place_name"));
            intent.putExtra("lat",jsonObject.getString("lat"));
            intent.putExtra("mlong",jsonObject.getString("mlong"));
            intent.putExtra("ac_image",jsonObject.getString("ac_image"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
        //scannerView.resumeCameraPreview(this);  /* If you want resume scanning, call this method */
        //resultText = null;
        //onBackPressed();
    }
}
