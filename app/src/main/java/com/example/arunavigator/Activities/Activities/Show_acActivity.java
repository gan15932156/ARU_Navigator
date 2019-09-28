package com.example.arunavigator.Activities.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.arunavigator.R;

public class Show_acActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ac);

        Toast.makeText(this, getIntent().getExtras().getString("ac_name"), Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.home:
                Intent intent = new Intent(Show_acActivity.this,MainActivity.class);
                finishAffinity();
                startActivity(intent);
                this.finish();
        }
        return  super.onOptionsItemSelected(item);
    }
}
