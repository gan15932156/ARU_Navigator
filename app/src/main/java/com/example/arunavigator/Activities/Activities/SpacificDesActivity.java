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

import com.example.arunavigator.Activities.Activities.Adapter.CustomAdapterFloorSpinner;
import com.example.arunavigator.Activities.Activities.Adapter.ManageSpDesAdapter;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.Activities.Activities.GetterSetter.SpacificDes;
import com.example.arunavigator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpacificDesActivity extends AppCompatActivity {
    private Context mContext;
    private RecyclerView sp_des_recyclerView;
    private Button btn_new_sp_des;
    private Spinner spn_location;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<Location> mLocationList;
    private ArrayList<SpacificDes> mSpDesList;
    private String location_id,location_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spacific_des);

        init();

        spn_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Location location = (Location) parent.getSelectedItem();
                sp_des_recyclerView.setAdapter(null);
                location_id = location.getLocation_id();
                location_name = location.getLocation_name();
                select_sp_des(location_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btn_new_sp_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location_id == null){
                    Toast.makeText(mContext, "กรุณาเลือกอาคาร", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(mContext,NewSpDesActivity.class);
                    intent.putExtra("id",location_id);
                    intent.putExtra("name",location_name);
                    startActivity(intent);
                }

            }
        });
    }
    private void select_sp_des(String location_id){
        Query query = FirebaseDatabase.getInstance().getReference("SpacificDes")
                .orderByChild("location_id")
                .equalTo(location_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mSpDesList.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        SpacificDes spacificDes = dataSnapshot1.getValue(SpacificDes.class);
                        mSpDesList.add(spacificDes);

                    }
                    ManageSpDesAdapter manageSpDesAdapter = new ManageSpDesAdapter(mContext,mSpDesList);
                    sp_des_recyclerView.setAdapter(manageSpDesAdapter);
                }
                else{ Toast.makeText(mContext, "ไม่พบข้อมูล", Toast.LENGTH_SHORT).show(); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void init(){
        mContext = SpacificDesActivity.this;
        sp_des_recyclerView = findViewById(R.id.manage_sp_des_recyclerview);
        sp_des_recyclerView.setHasFixedSize(true);
        sp_des_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("SpacificDes");
        mLocationList = new ArrayList<>();
        mSpDesList = new ArrayList<>();
        btn_new_sp_des = findViewById(R.id.manage_sp_des_btn_new_sp_des);
        spn_location = findViewById(R.id.manage_sp_des_spn_location);
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.home,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.home:
                this.finishAffinity();
                startActivity(new Intent(SpacificDesActivity.this,IndexAdminActivity.class));
        }
        return  super.onOptionsItemSelected(item);
    }
}
