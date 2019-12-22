package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arunavigator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewVertexActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextInputEditText edit_vertex_name,edit_vertex_number;
    private Spinner spn_vertex_type,spn_vertex_path_type,spn_building_name;
    private Button btn_insert,btn_reload;
    private Context mContext;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private String lat,mlong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vertex);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private void init(){
        mContext = NewVertexActivity.this;
        edit_vertex_name = findViewById(R.id.new_vertex_edit_vertex_name);
        edit_vertex_number = findViewById(R.id.new_vertex_edit_vertex_number);
        spn_building_name = findViewById(R.id.new_vertex_spn_building_name);
        spn_vertex_path_type = findViewById(R.id.new_vertex_spn_vertex_path_type);
        spn_vertex_type = findViewById(R.id.new_vertex_spn_vertex_type);
        btn_insert = findViewById(R.id.new_vertex_btn_insert_vertex);
        btn_reload = findViewById(R.id.new_vertex_btn_reload_data);

        ArrayAdapter<CharSequence> type_adapter = ArrayAdapter.createFromResource(this,R.array.sp_vertex_type,android.R.layout.simple_spinner_item);
        spn_vertex_type.setAdapter(type_adapter);

        ArrayAdapter<CharSequence> path_type_adapter = ArrayAdapter.createFromResource(this,R.array.sp_vertex_path_type,android.R.layout.simple_spinner_item);
        spn_vertex_path_type.setAdapter(path_type_adapter);

        load_building_data();

    }
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
            //editStart.setText(location.getLatitude()+","+location.getLongitude());
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

    private void send(){

    }
    private void reload(){

    }
    private void load_building_data(){
        Query query = FirebaseDatabase.getInstance().getReference("Building");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final List<String> building_list = new ArrayList<>();
                building_list.add("-");
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String building_name =dataSnapshot1.child("location_name").getValue(String.class);
                    building_list.add(building_name);
                    MarkerOptions marker = new MarkerOptions()
                            .title(building_name)
                            .position(new LatLng(
                                Double.parseDouble(dataSnapshot1.child("lat").getValue(String.class)),
                                    Double.parseDouble(dataSnapshot1.child("longti").getValue(String.class))
                            ))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                    mMap.addMarker(marker);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, building_list);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_building_name.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
