package com.example.arunavigator.Activities.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.arunavigator.Activities.Activities.Adapter.ManageLocationAdapter;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageLocationActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_new_location;
    private RecyclerView location_recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<Location> mLocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_location);
        init();
        load_location();
    }
    private void init(){
        mContext = ManageLocationActivity.this;
        btn_new_location = findViewById(R.id.manage_location_btn_new_location);
        location_recyclerView = findViewById(R.id.manage_location_recyclerview);
        location_recyclerView.setHasFixedSize(true);
        location_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Building");
        mLocationList = new ArrayList<>();

    }
    private void load_location(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLocationList.clear();
                for(DataSnapshot postSnapshot  : dataSnapshot.getChildren()){
                    Location location = postSnapshot.getValue(Location.class);
                    mLocationList.add(location);

                    Log.d("kuayyyyyyyyyy",mLocationList.toString());
                }
                ManageLocationAdapter adapter = new ManageLocationAdapter(ManageLocationActivity.this,mLocationList);
                location_recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
