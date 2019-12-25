package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.Edge;
import com.example.arunavigator.Activities.Activities.GetterSetter.Vertex;
import com.example.arunavigator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class NewEdgeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context mContext;
    private Button btn_reload,btn_load_data,btn_back;
    private Spinner spn_vertex_path_type;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Polyline polyline;
    private HashMap<String,String> myHashVertexBase;
    private Vertex vertex;
    private List<Vertex> vertices = new ArrayList<>();;
    private ArrayList<HashMap<String,String>> myListVertexBase = new ArrayList<HashMap<String, String>>();
    private List<Edge> edges = new ArrayList<>();
    private Edge edge;
    private ArrayList<HashMap<String,String>> myArrList = new ArrayList<HashMap<String, String>>();
    private HashMap<String,String> map;
    private String vertex_path_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edge);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

        spn_vertex_path_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vertex_path_type = spn_vertex_path_type.getSelectedItem().toString();
                load_vertex_by_path_type(vertex_path_type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        btn_load_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_edge();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,IndexAdminActivity.class));
                finishAffinity();
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

        LatLng sydney = new LatLng(14.3472549, 100.5653264);
        mMap.moveCamera(newLatLngZoom(sydney, 18));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Double distance = 0.0;
                ArrayList<String> arrList = new ArrayList<String>();
                ArrayList<LatLng> arrListLatLng = new ArrayList<LatLng>();
                map = new HashMap<String, String>();
                map.put("vertex_name",marker.getTitle());
                map.put("lat",String.valueOf(marker.getPosition().latitude));
                map.put("long",String.valueOf(marker.getPosition().longitude));
                myArrList.add(map);
                MarkerOptions options = new MarkerOptions();
                options.position(marker.getPosition());

                if (myArrList.size() == 0) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                else if (myArrList.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                }
                if (myArrList.size() > 1) {
                    for(int j=0;j<myArrList.size();j++){
                        //Toast.makeText(mContext, myArrList.get(j).get("vertex_name"), Toast.LENGTH_SHORT).show();
                        for(int i =0;i<myListVertexBase.size();i++){
                            //Log.d("TAGGGGGGG",myListVertexBase.get(i).get("vertex_name"));
                            //Log.d("TAGGGGGGG",myArrList.get(j).get("vertex_name")+" "+myListVertexBase.get(i).get("vertex_name"));
                            if(myArrList.get(j).get("vertex_name").equals(myListVertexBase.get(i).get("vertex_name"))){
                                //Toast.makeText(mContext, myListVertexBase.get(i).get("vertex_name")+" "+myListVertexBase.get(i).get("vertex_number"), Toast.LENGTH_SHORT).show();
                                //Log.d("TAGGGGGGG",myListVertexBase.get(i).get("vertex_name")+" "+myListVertexBase.get(i).get("vertex_number"));
                                arrList.add(myListVertexBase.get(i).get("vertex_number"));
                                arrListLatLng.add(new LatLng(
                                        Double.parseDouble(myListVertexBase.get(i).get("vertex_lat")),
                                        Double.parseDouble(myListVertexBase.get(i).get("vertex_long"))

                                ));
                            }
                        }
                    }
                    mRef = mDatabase.getReference("Edge");
                    String edge_id = mRef.push().getKey();

                    distance = SphericalUtil.computeDistanceBetween(
                            new LatLng(
                                    arrListLatLng.get(0).latitude,
                                    arrListLatLng.get(0).longitude
                            ),
                            new LatLng(
                                    arrListLatLng.get(1).latitude,
                                    arrListLatLng.get(1).longitude
                            )

                    );

                    Edge edge = new Edge(
                            edge_id,
                            arrList.get(0),
                            arrList.get(1),
                            vertex_path_type,
                            String.valueOf(Math.round(distance))

                    );
                    mRef.child(edge_id).setValue(edge);

                    String edge_id2 = mRef.push().getKey();
                    Edge edge1 = new Edge(
                            edge_id2,
                            arrList.get(1),
                            arrList.get(0),
                            vertex_path_type,
                            String.valueOf(Math.round(distance))
                    );
                    mRef.child(edge_id2).setValue(edge1);

                    arrList.clear();
                    arrListLatLng.clear();
                    myArrList.clear();
                }
                mMap.addMarker(options);
                return false;
            }
        });
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
        mDatabase = FirebaseDatabase.getInstance();
        btn_reload = findViewById(R.id.new_edge_btn_reload);
        btn_load_data = findViewById(R.id.new_edge_btn_load_data);
        spn_vertex_path_type = findViewById(R.id.new_edge_spn_vertex_type);
        ArrayAdapter<CharSequence> path_type_adapter = ArrayAdapter.createFromResource(this,R.array.sp_vertex_path_type,android.R.layout.simple_spinner_item);
        spn_vertex_path_type.setAdapter(path_type_adapter);
        btn_back = findViewById(R.id.new_edge_btn_back);
    }

    private void load_vertex_by_path_type(final String vertex_path_type){
        mMap.clear();
        vertices.clear();
        myListVertexBase.clear();
        edges.clear();
        load_building_data();
        Query query = FirebaseDatabase.getInstance().getReference("Vertex")
                .orderByChild("vertex_path_type")
                .equalTo(vertex_path_type);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(
                            Double.parseDouble(dataSnapshot1.child("vertex_lat").getValue(String.class)),
                                Double.parseDouble(dataSnapshot1.child("vertex_long").getValue(String.class))
                        ))
                                .title(dataSnapshot1.child("vertex_name").getValue(String.class))
                                .snippet(dataSnapshot1.child("vertex_number").getValue(String.class));

                        if(vertex_path_type.equals("ทางเดินรถ")){
                           markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        }
                        else{
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                        }
                        myHashVertexBase = new HashMap<String, String>();
                        mMap.addMarker(markerOptions);
                        vertex = new Vertex(
                                dataSnapshot1.child("vertex_id").getValue(String.class),
                                dataSnapshot1.child("vertex_name").getValue(String.class),
                                dataSnapshot1.child("vertex_lat").getValue(String.class),
                                dataSnapshot1.child("vertex_long").getValue(String.class),
                                dataSnapshot1.child("vertex_number").getValue(String.class),
                                dataSnapshot1.child("vertex_type").getValue(String.class),
                                dataSnapshot1.child("vertex_building_name").getValue(String.class),
                                dataSnapshot1.child("vertex_path_type").getValue(String.class)
                        );
                        vertices.add(vertex);
                        myHashVertexBase.put("vertex_id",dataSnapshot1.child("vertex_id").getValue(String.class));
                        myHashVertexBase.put("vertex_lat",dataSnapshot1.child("vertex_lat").getValue(String.class));
                        myHashVertexBase.put("vertex_long",dataSnapshot1.child("vertex_long").getValue(String.class));
                        myHashVertexBase.put("vertex_name",dataSnapshot1.child("vertex_name").getValue(String.class));
                        myHashVertexBase.put("vertex_number",dataSnapshot1.child("vertex_number").getValue(String.class));
                        myListVertexBase.add(myHashVertexBase);
                    }
                }
                else{
                    Toast.makeText(mContext, "ไม่สามารถเรียกข้อมูล Vertex ได้", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        query = FirebaseDatabase.getInstance().getReference("Edge")
                .orderByChild("vertex_path_type")
                .equalTo(vertex_path_type);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        edge  = new Edge(
                                dataSnapshot1.child("edge_id").getValue(String.class),
                                dataSnapshot1.child("edge_source").getValue(String.class),
                                dataSnapshot1.child("edge_destination").getValue(String.class),
                                dataSnapshot1.child("vertex_path_type").getValue(String.class),
                                dataSnapshot1.child("distance").getValue(String.class)
                        );
                        edges.add(edge);
                    }
                }
                else{
                    Toast.makeText(mContext, "ไม่สามารถเรียกข้อมูล Edge ได้", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    private void load_building_data(){
        Query query = FirebaseDatabase.getInstance().getReference("Building");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        String building_name =dataSnapshot1.child("location_name").getValue(String.class);
                        MarkerOptions marker = new MarkerOptions()
                                .title(building_name)
                                .position(new LatLng(
                                        Double.parseDouble(dataSnapshot1.child("lat").getValue(String.class)),
                                        Double.parseDouble(dataSnapshot1.child("longti").getValue(String.class))
                                ));
                        mMap.addMarker(marker);
                    }  
                }
                else{
                    Toast.makeText(mContext, "ไม่สามารถเรียกข้อมูลอาคารและสถานที่ได้", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    private void show_edge(){
        String s_lat = null;
        String s_long = null;
        String d_lat = null;
        String d_long = null;
        // show edge with check if
        if(!edges.get(0).getEdge_id().isEmpty()){
            for (int i = 0 ; i < edges.size() ; i++){
                for (int j = 0 ; j < vertices.size() ; j++){
                    if(vertices.get(j).getVertex_number().equals(edges.get(i).getEdge_source())){
                        s_lat = vertices.get(j).getVertex_lat();
                        s_long = vertices.get(j).getVertex_long();
                    }
                    if(vertices.get(j).getVertex_number().equals(edges.get(i).getEdge_destination())){
                        d_lat = vertices.get(j).getVertex_lat();
                        d_long = vertices.get(j).getVertex_long();
                    }
                }
                polyline = mMap.addPolyline(new PolylineOptions()
                        .add(
                                new LatLng(Double.parseDouble(s_lat),Double.parseDouble(s_long)),
                                new LatLng(Double.parseDouble(d_lat),Double.parseDouble(d_long))
                        )
                        .width(5)
                        .color(Color.RED)
                );
            }
        }
        else{
            Toast.makeText(mContext, "ไม่พบข้อมูล Edge", Toast.LENGTH_SHORT).show();
        }
    }
    private void reload(){
        startActivity(new Intent(mContext,NewEdgeActivity.class));
        this.finishAffinity();
    }

}
