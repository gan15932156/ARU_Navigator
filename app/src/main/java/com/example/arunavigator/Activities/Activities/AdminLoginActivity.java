package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.arunavigator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {
    private TextInputEditText edit_username,edit_password;
    private Button btn_submit;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        init();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edit_username.getText().toString().trim();
                String password = edit_password.getText().toString().trim();
                if(!username.isEmpty() && !password.isEmpty()){
                    submit_login(username,password);
                }
                else{
                    Toast.makeText(AdminLoginActivity.this, "กรุณาข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void init(){
        edit_username = findViewById(R.id.admin_login_edit_username);
        edit_password = findViewById(R.id.admin_login_edit_password);
        btn_submit = findViewById(R.id.admin_login_btn_submit);
        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void submit_login(String username,String password){
        final ProgressDialog ps = new ProgressDialog(this);
        ps.setTitle("กรุณารอ");
        ps.show();
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ps.dismiss();
                    finishAffinity();
                    startActivity(new Intent(AdminLoginActivity.this, IndexAdminActivity.class));
                    finish();
                }
                else{
                    ps.dismiss();
                    Toast.makeText(AdminLoginActivity.this, "ล้มเหลว", Toast.LENGTH_SHORT).show();
                    edit_password.setText(null);
                    edit_username.setText(null);
                }
            }
        });
    }
}
