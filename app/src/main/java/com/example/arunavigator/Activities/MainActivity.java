package com.example.arunavigator.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.arunavigator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements 
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    private EditText edit_quest;
    private Button btn_submit_quest,btn_choose_path,btn_view_location;
    private SliderLayout sliderLayout;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private HashMap<String, String> HashMapForURL ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) { StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mDatabase = FirebaseDatabase.getInstance();
        initView();
        showSlider();

        btn_submit_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_choose_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.scan_qr_code:

        }
        return  super.onOptionsItemSelected(item);
    }
    public void initView(){
      edit_quest = findViewById(R.id.main_edit_quest);
      btn_submit_quest = findViewById(R.id.main_btn_quest_submit);
      btn_choose_path = findViewById(R.id.main_btn_choose_path);
      btn_view_location = findViewById(R.id.main_btn_view_location);
      sliderLayout = findViewById(R.id.main_activity_slider);
    }
    public void showSlider(){
        HashMapForURL = new HashMap<String, String>();
        mRef = mDatabase.getReference("Activity");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    HashMapForURL.put(postSnapshot.child("ac_name").getValue(String.class),postSnapshot.child("ac_image").getValue(String.class));
                    TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                    textSliderView
                            .description(postSnapshot.child("ac_name").getValue(String.class))
                            .image(postSnapshot.child("ac_image").getValue(String.class))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener((BaseSliderView.OnSliderClickListener) getApplicationContext());
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("id",postSnapshot.child("ac_id").getValue(String.class));
                    sliderLayout.addSlider(textSliderView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(MainActivity.this);
    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, "kuay", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) { }
}
