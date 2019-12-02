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
import android.widget.Button;

import com.example.arunavigator.Activities.Activities.Adapter.ManageActAdapter;
import com.example.arunavigator.Activities.Activities.Adapter.ManageLocationAdapter;
import com.example.arunavigator.Activities.Activities.GetterSetter.ActivitiesClass;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageAcActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_new_act;
    private RecyclerView act_recyclerView;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private ArrayList<ActivitiesClass> mActList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_ac);

        init();
        load_act();

        btn_new_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,NewActActivity.class));
            }
        });
    }
    private void init(){
        mContext = ManageAcActivity.this;
        btn_new_act = findViewById(R.id.manage_ac_btn_new_act);
        act_recyclerView = findViewById(R.id.manage_ac_recyclerview);
        act_recyclerView.setHasFixedSize(true);
        act_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("Activity");
        mActList = new ArrayList<>();
    }
    private void load_act(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mActList.clear();
                for(DataSnapshot postSnapshot  : dataSnapshot.getChildren()){
                    ActivitiesClass activitiesClass = postSnapshot.getValue(ActivitiesClass.class);
                    mActList.add(activitiesClass);

                }
                ManageActAdapter adapter = new ManageActAdapter(mContext,mActList);
                act_recyclerView.setAdapter(adapter);
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
                startActivity(new Intent(ManageAcActivity.this,IndexAdminActivity.class));
        }
        return  super.onOptionsItemSelected(item);
    }
}
