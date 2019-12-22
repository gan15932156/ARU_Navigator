package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.Adapter.CustomAdapterFloorNumberSpinner;
import com.example.arunavigator.Activities.Activities.Adapter.CustomAdapterFloorSpinner;
import com.example.arunavigator.Activities.Activities.Adapter.ManageToiletAdapter;
import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.Activities.Activities.GetterSetter.Toilet;
import com.example.arunavigator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageToiletActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView toilet_recyclerView;
    private Button btn_new_toilet;
    private Spinner spn_location,spn_floor;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<Location> mLocationList;
    private ArrayList<Floor> mFloorList;
    private ArrayList<Toilet> mToiletList;
    private String location_id,location_name,floor_id,floor_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_toilet);

        init();

        spn_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Location location = (Location) parent.getSelectedItem();
                toilet_recyclerView.setAdapter(null);
                location_id = location.getLocation_id();
                location_name = location.getLocation_name();
                spn_floor.setAdapter(null);
                select_floor(location_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_floor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Floor floor = (Floor) parent.getSelectedItem();
                toilet_recyclerView.setAdapter(null);
                floor_id = floor.getFloor_id();
                floor_number = floor.getFloor_number();

                select_toilet(floor_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_new_toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location_id == null && floor_id == null){
                    Toast.makeText(mContext, "กรุณาเลือกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(mContext,NewToiletActivity.class);

                    intent.putExtra("floor_id",floor_id);
                    intent.putExtra("location_name",location_name);
                    intent.putExtra("floor_number",floor_number);
                    startActivity(intent);
                }

            }
        });
    }

    private void init(){
        mContext = ManageToiletActivity.this;
        toilet_recyclerView = findViewById(R.id.manage_toilet_recyclerview);
        toilet_recyclerView.setHasFixedSize(true);
        toilet_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Toilet");
        mLocationList = new ArrayList<>();
        mFloorList = new ArrayList<>();
        mToiletList = new ArrayList<>();
        btn_new_toilet = findViewById(R.id.manage_toilet_btn_new_toilet);
        spn_location = findViewById(R.id.manage_toilet_spn_location);
        spn_floor = findViewById(R.id.manage_toilet_spn_floor);
        load_location_spn();
        location_id = null;
        floor_id = null;
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
                    CustomAdapterFloorNumberSpinner customAdapterFloorNumberSpinner = new CustomAdapterFloorNumberSpinner(mContext,mFloorList);
                    spn_floor.setAdapter(customAdapterFloorNumberSpinner);
                }
                else{ Toast.makeText(mContext, "ไม่พบชั้นของอาคารที่เลือก", Toast.LENGTH_SHORT).show(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void load_location_spn(){
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
    private void select_toilet(String floor_id){
        Query query = FirebaseDatabase.getInstance().getReference("Toilet")
                .orderByChild("floor_id")
                .equalTo(floor_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mToiletList.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Toilet toilet = dataSnapshot1.getValue(Toilet.class);
                        mToiletList.add(toilet);

                    }
                    ManageToiletAdapter manageToiletAdapter = new ManageToiletAdapter(mContext,mToiletList);
                    toilet_recyclerView.setAdapter(manageToiletAdapter);
                }
                else{ Toast.makeText(mContext, "ไม่พบห้องสุขาของอาคารที่เลือก", Toast.LENGTH_SHORT).show(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
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
                startActivity(new Intent(ManageToiletActivity.this,IndexAdminActivity.class));
        }
        return  super.onOptionsItemSelected(item);
    }
}
