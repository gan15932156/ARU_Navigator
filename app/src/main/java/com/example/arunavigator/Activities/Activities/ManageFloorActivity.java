package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.Adapter.CustomAdapterFloorSpinner;
import com.example.arunavigator.Activities.Activities.Adapter.ManageFloorAdapter;
import com.example.arunavigator.Activities.Activities.Adapter.ManageLocationAdapter;
import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageFloorActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView floor_recyclerView;
    private Button btn_new_floor;
    private Spinner spn_location;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<Location> mLocationList;
    private String location_id,location_name;
    private ArrayList<Floor> mFloorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_floor);

        init();
        //load_location();

        spn_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Location location = (Location) parent.getSelectedItem();
                floor_recyclerView.setAdapter(null);
                location_id = location.getLocation_id();
                location_name = location.getLocation_name();
                select_floor(location_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_new_floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location_id == null){
                    Toast.makeText(mContext, "กรุณาเลือกอาคาร", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(mContext,NewFloorActivity.class);
                    intent.putExtra("id",location_id);
                    intent.putExtra("name",location_name);
                    startActivity(intent);
                }

            }
        });
    }
    private void select_floor(String location_id){
        Query query = FirebaseDatabase.getInstance().getReference("Floor")
                .orderByChild("location_id")
                .equalTo(location_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mFloorList.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Floor floor = dataSnapshot1.getValue(Floor.class);
                        mFloorList.add(floor);

                    }
                    ManageFloorAdapter manageFloorAdapter = new ManageFloorAdapter(mContext,mFloorList);
                    floor_recyclerView.setAdapter(manageFloorAdapter);
                }
                else{ Toast.makeText(mContext, "ไม่พบชั้นของอาคารที่เลือก", Toast.LENGTH_SHORT).show(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void init(){
        mContext = ManageFloorActivity.this;
        floor_recyclerView = findViewById(R.id.manage_floor_recyclerview);
        floor_recyclerView.setHasFixedSize(true);
        floor_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Building");
        mLocationList = new ArrayList<>();
        mFloorList = new ArrayList<>();
        btn_new_floor = findViewById(R.id.manage_floor_btn_new_floor);
        spn_location = findViewById(R.id.manage_floor_spn_location);
        load_location_son();
        location_id = null;

    }
    private void load_location_son(){
        Query query = FirebaseDatabase.getInstance().getReference("Building")
                .orderByChild("type")
                .equalTo("อาคาร");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLocationList.clear();
                for(DataSnapshot postSnapshot  : dataSnapshot.getChildren()){
                    Location locationn = postSnapshot.getValue(Location.class);
                    mLocationList.add(locationn);
                }
                CustomAdapterFloorSpinner customAdapterFloorSpinner = new CustomAdapterFloorSpinner(mContext,mLocationList);
                spn_location.setAdapter(customAdapterFloorSpinner);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void load_location(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLocationList.clear();
                for(DataSnapshot postSnapshot  : dataSnapshot.getChildren()){
                    Location location = postSnapshot.getValue(Location.class);
                    mLocationList.add(location);
                }
                ManageLocationAdapter adapter = new ManageLocationAdapter(ManageFloorActivity.this,mLocationList);
                floor_recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.home:
                this.finishAffinity();
                startActivity(new Intent(ManageFloorActivity.this,IndexAdminActivity.class));
        }
        return  super.onOptionsItemSelected(item);
    }
}
