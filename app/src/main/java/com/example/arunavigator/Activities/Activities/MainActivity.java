package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.arunavigator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.example.arunavigator.R.layout.dialog_main_show_activity;
import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class MainActivity extends AppCompatActivity implements 
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {
    private EditText edit_quest;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button btn_submit_quest,btn_choose_path,btn_view_location;
    private SliderLayout sliderLayout;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private HashMap<String, String> HashMapForURL ;
    private GoogleMap mMap;
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
                if(!edit_quest.getText().toString().isEmpty()){

                }
                else{
                    Toast.makeText(MainActivity.this, "กรุณากรอก", Toast.LENGTH_SHORT).show();
                }
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
                startActivity(new Intent(MainActivity.this,FindPathActivity.class));
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home_login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.admin_menu_scan_qr_code:
                startActivity(new Intent(MainActivity.this,QR_code_scan.class));
            case R.id.login_admin:
                this.finishAffinity();
                startActivity(new Intent(MainActivity.this,AdminLoginActivity.class));
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
                            .setOnSliderClickListener(MainActivity.this);
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
        sliderLayout.startAutoCycle();
    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(final BaseSliderView slider) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        final View view = layoutInflater.inflate(dialog_main_show_activity,null);
        mBuilder.setView(view);

        final ImageView img_view = view.findViewById(R.id.main_dialog_image);
        final TextView txt_ac_name = view.findViewById(R.id.main_dialog_txt_ac_name);
        final TextView txt_ac_des = view.findViewById(R.id.main_dialog_txt_ac_des);
        final TextView txt_ac_place = view.findViewById(R.id.main_dialog_txt_ac_place);
        final MapView mapView = view.findViewById(R.id.main_dialog_mapView);
        MapsInitializer.initialize(MainActivity.this);
        mapView.onCreate(new Bundle());
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
                mMap.setOnMyLocationClickListener(onMyLocationClickListener);
                enableMyLocationIfPermitted();
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                Query query = FirebaseDatabase.getInstance().getReference("Activity")
                        .orderByChild("ac_id")
                        .equalTo(slider.getBundle().get("id").toString());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            txt_ac_name.setText(dataSnapshot1.child("ac_name").getValue(String.class));
                            txt_ac_des.setText("รายละเอียด: "+dataSnapshot1.child("ac_des").getValue(String.class));
                            txt_ac_place.setText("สถานที่: "+dataSnapshot1.child("ac_place_name").getValue(String.class));
                            Picasso.with(MainActivity.this).load(dataSnapshot1.child("ac_image").getValue(String.class)).fit().centerCrop().into(img_view);

                            LatLng latLng = new LatLng(Double.parseDouble(dataSnapshot1.child("lat").getValue(String.class)), Double.parseDouble(dataSnapshot1.child("mlong").getValue(String.class)));
                            MarkerOptions marker = new MarkerOptions().position(latLng).title(dataSnapshot1.child("ac_place_name").getValue(String.class));
                            mMap.moveCamera(newLatLngZoom(latLng, 18));
                            mMap.addMarker(marker);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        int width = (int) (getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int) (getResources().getDisplayMetrics().heightPixels*0.80);
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.getWindow().setLayout(width,height);
        alertDialog.show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

    @Override
    public void onPageSelected(int position) { }

    @Override
    public void onPageScrollStateChanged(int state) { }
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) { mMap.setMyLocationEnabled(true); }
    }
    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener = new GoogleMap.OnMyLocationButtonClickListener() {
        @Override
        public boolean onMyLocationButtonClick() { return false; }
    };
    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener = new GoogleMap.OnMyLocationClickListener() {
        @Override
        public void onMyLocationClick(@NonNull Location location) {
            //autoStart.setText("I'm here");
        }
    };
    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " + "showing default location", Toast.LENGTH_SHORT).show();
        LatLng build100 = new LatLng(14.3472549, 100.5653264);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(build100));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else { showDefaultLocation(); }
                return;
            }
        }
    }
}
