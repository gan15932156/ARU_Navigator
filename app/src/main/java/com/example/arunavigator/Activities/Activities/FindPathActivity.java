package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.GetterSetter.Edge_cal;
import com.example.arunavigator.Activities.Activities.GetterSetter.Graph;
import com.example.arunavigator.Activities.Activities.GetterSetter.Vertex_cal;
import com.example.arunavigator.Activities.Activities.Helper.DijkstraAlgorithm;
import com.example.arunavigator.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

public class FindPathActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btn_find_path;
    private Spinner spn;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private AutoCompleteTextView autoStart,autoEnd;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private Polyline line;
    private LinkedList<Vertex_cal> path ;
    private TextView txtDistance,txtDuration;
    private Context mContext;
    private List<String> building_list;

    private List<Vertex_cal> vertex_cals = new ArrayList<>();
    private Vertex_cal vertex_cal ;
    private Edge_cal edge_cal;
    private List<Edge_cal> edge_cals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_path);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();

        btn_find_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoStart.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "กรุณากรอกจุดเริ่มต้น", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(autoEnd.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "กรุณากรอกจุดหมาย", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!autoStart.getText().toString().isEmpty() && !autoEnd.getText().toString().isEmpty()){
                    cal_path(autoStart.getText().toString(),autoEnd.getText().toString());
                }
            }
        });
        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                load_vertex_and_edge(spn.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng ARU = new LatLng(14.3496251, 100.5632134);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ARU));
        mMap.moveCamera(newLatLngZoom(ARU, 18));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final Dialog dialog = new Dialog(mContext);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setContentView(R.layout.show_dialog_location_choose_destination);

                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
                dialog.getWindow().setLayout(width,height);

                final TextView txt_name = dialog.findViewById(R.id.txt_dialog_show_location_name);
                final TextView txt_des = dialog.findViewById(R.id.txt_dialog_show_location_des);
                final Button btn_back = dialog.findViewById(R.id.btn_show_location_back);
                final ImageView img = dialog.findViewById(R.id.image_show_location_dialog);

                Query query = FirebaseDatabase.getInstance().getReference("Building")
                        .orderByChild("location_name")
                        .equalTo(marker.getTitle());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            Picasso.with(mContext)
                                    .load(dataSnapshot1.child("image_path").getValue(String.class))
                                    .fit()
                                    .centerCrop()
                                    .into(img);
                            txt_name.setText(dataSnapshot1.child("location_name").getValue(String.class));
                            txt_des.setText(dataSnapshot1.child("location_des").getValue(String.class));
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                if(autoStart.getText().toString().isEmpty()){
                    //marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    autoStart.setText(marker.getTitle());
                }
                if(!autoStart.getText().toString().isEmpty()){
                    //marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    autoEnd.setText(marker.getTitle());
                }
                if(autoEnd.getText().toString().isEmpty()){
                    //marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                    autoEnd.setText(marker.getTitle());
                }
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
        mContext = FindPathActivity.this;
        autoStart = findViewById(R.id.find_path_autoText_start);
        autoEnd = findViewById(R.id.find_path_autoText_end);
        txtDistance = findViewById(R.id.find_path_tvDistance);
        txtDuration = findViewById(R.id.find_path_tvDuration);
        btn_find_path = findViewById(R.id.find_path_btnFindPath);
        spn = findViewById(R.id.find_path_spn_path_type);
        ArrayAdapter<CharSequence> path_type_adapter = ArrayAdapter.createFromResource(this,R.array.sp_vertex_path_type,android.R.layout.simple_spinner_item);
        spn.setAdapter(path_type_adapter);
        mDatabase = FirebaseDatabase.getInstance();
        building_list = new ArrayList<>();

        load_building();
    }

    private void cal_path(String source,String destination){
        /*for (int i = 0 ; i < vertex_cals.size() ; i++){
            Log.d("TAGGGGGGG",vertex_cals.get(i).getVertex_name());
        }*/

        ArrayList<String> lo_name = new ArrayList<String>();

        lo_name.add(source);
        lo_name.add(destination);
        int number_source = 0;
        int number_des = 0;

        for(int i=0;i<vertex_cals.size();i++){ //หา number vertex ของจุดเริ่มต้น-จุดหมาย
            if(vertex_cals.get(i).getVertex_building_name().equals(lo_name.get(0))){
                number_source = Integer.parseInt(vertex_cals.get(i).getVertex_number());
            }
            if(vertex_cals.get(i).getVertex_building_name().equals(lo_name.get(1))){
                number_des = Integer.parseInt(vertex_cals.get(i).getVertex_number());
            }
        }
        Log.d("TAGGGG","number source "+number_source+" number des "+number_des);
        //Log.d("TAGGGGG",number_source+" "+number_des);
        int path_source = 0;
        int path_Destination = 0;
        for (int jj = 0 ; jj < vertex_cals.size() ; jj++ ){
            if(vertex_cals.get(jj).getVertex_number().equals(String.valueOf(number_source))){
                path_source = jj ;
            }
            if(vertex_cals.get(jj).getVertex_number().equals(String.valueOf(number_des))){
                path_Destination = jj ;
            }
        }
        Log.d("TAGGGG",vertex_cals.get(path_source)+"");
        Log.d("TAGGGG",vertex_cals.get(path_Destination)+"");
        //Toast.makeText(mContext, path_source+" "+path_Destination, Toast.LENGTH_SHORT).show();

        Graph graph = new Graph(vertex_cals, edge_cals); //init
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph); //init

        dijkstra.execute(vertex_cals.get(path_source)); // start
        path = dijkstra.getPath(vertex_cals.get(path_Destination)); //stop

        int cal = 0;
        double distance_km = 0.0;
        double time = 0.0;
        int speed = 5 ;

        String s_lat = null;
        String s_long = null;
        String d_lat = null;
        String d_long = null;
        if(path != null){
            for(int i =0;i<path.size();i++){
                if(path.size() == i+1){
                    break;
                }
                else{
                    //Log.d("TAGGGG",path.get(i).getVertex_number()+" "+path.get(i).getVertex_name());
                    for (int j=0;j<edge_cals.size();j++){
                        //Log.d("TAGGGGG",path)
                        if(path.get(i).getVertex_name() == edge_cals.get(j).getEdge_cal_source().getVertex_name() &&
                                path.get(i+1).getVertex_name() == edge_cals.get(j).getEdge_cal_destination().getVertex_name()
                        ){
                            cal+=edge_cals.get(j).getWeight();
                            Log.d("TAGGGGGGGGGGGGGGGGGGGG",
                                    "From "+
                                            edge_cals.get(j).getEdge_cal_source().getVertex_name()
                                            +" To "+edge_cals.get(j).getEdge_cal_destination().getVertex_name()
                                            +" distance "+edge_cals.get(j).getWeight()
                            );
                            line = mMap.addPolyline(new PolylineOptions()
                                    .add(
                                            new LatLng(
                                                    Double.parseDouble(edge_cals.get(j).getEdge_cal_source().getVertex_lat()),
                                                    Double.parseDouble(edge_cals.get(j).getEdge_cal_source().getVertex_long())
                                            ),
                                            new LatLng(
                                                    Double.parseDouble(edge_cals.get(j).getEdge_cal_destination().getVertex_lat()),
                                                    Double.parseDouble(edge_cals.get(j).getEdge_cal_destination().getVertex_long())
                                            )
                                    )
                                    .width(10)
                                    .color(Color.GREEN));
                        }
                    }
                    /*Log.d("TAGGGG",path.get(i).getVertex_name());
                    Log.d("TAGGGG",path.get(i).getVertex_name()+" "+path.get(i).getVertex_building_name());*/
                }

            }
            distance_km = (double) cal / 1000;
            time = (distance_km / speed) * 60;
            String s = String.format(("%.2f"),time);
            txtDuration.setText(s+" นาที");
            txtDistance.setText(cal+" เมตร");
        }
        else{
            Toast.makeText(mContext, "ไม่สามารถค้นหาเส้นทางที่เลือกได้", Toast.LENGTH_SHORT).show();
        }
    }
    private void load_vertex_and_edge(String vertex_path_type){

        Query query = FirebaseDatabase.getInstance().getReference("Vertex")
                .orderByChild("vertex_path_type")
                .equalTo(vertex_path_type);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    vertex_cals.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        vertex_cal  = new Vertex_cal(
                                dataSnapshot1.child("vertex_id").getValue(String.class),
                                dataSnapshot1.child("vertex_name").getValue(String.class),
                                dataSnapshot1.child("vertex_lat").getValue(String.class),
                                dataSnapshot1.child("vertex_long").getValue(String.class),
                                dataSnapshot1.child("vertex_number").getValue(String.class),
                                dataSnapshot1.child("vertex_type").getValue(String.class),
                                dataSnapshot1.child("vertex_building_name").getValue(String.class),
                                dataSnapshot1.child("vertex_path_type").getValue(String.class)
                        );
                        vertex_cals.add(vertex_cal);
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
                    edge_cals.clear();
                    for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                        edge_cal = new Edge_cal(
                                dataSnapshot2.child("edge_id").getValue(String.class),
                                vertex_cals.get(Integer.parseInt(dataSnapshot2.child("edge_source").getValue(String.class))),
                                vertex_cals.get(Integer.parseInt(dataSnapshot2.child("edge_destination").getValue(String.class))),
                                Integer.parseInt(dataSnapshot2.child("distance").getValue(String.class))
                        );
                        edge_cals.add(edge_cal);
                    }
                }
                else{
                    Toast.makeText(mContext, "ไม่สามารถเรียกข้อมูล Edge ได้", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }
    private void load_building(){
        mRef = mDatabase.getReference("Building");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Double lattitude = Double.parseDouble(dataSnapshot1.child("lat").getValue(String.class));
                        Double longtitude = Double.parseDouble(dataSnapshot1.child("longti").getValue(String.class));
                        String name =dataSnapshot1.child("location_name").getValue(String.class);

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(lattitude, longtitude)).title(name)/*.snippet(id)*//*  .icon(BitmapDescriptorFactory.fromBitmap(reSizeimage(image,200,200)))*/;
                        Marker m1 =  mMap.addMarker(marker);
                        m1.showInfoWindow();

                        building_list.add(name);
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,android.R.id.text1,building_list);
                    // bind dapter to autocompleteview
                    autoEnd.setAdapter(adapter);
                    autoStart.setAdapter(adapter);
                }
                else{
                    Toast.makeText(mContext, "ไม่สามารถแสดงสถานที่และอาคารได้", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
