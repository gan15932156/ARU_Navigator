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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arunavigator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class NewEdgeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context mContext;
    private Button btn_reload;
    private Spinner spn_vertex_path_type;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edge);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng sydney = new LatLng(14.3472549, 100.5653264);
        mMap.moveCamera(newLatLngZoom(sydney, 15));



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
    private void init(){
        mContext = NewEdgeActivity.this;
        btn_reload = findViewById(R.id.new_edge_btn_reload);
        spn_vertex_path_type = findViewById(R.id.new_edge_spn_vertex_type);

    }
}
