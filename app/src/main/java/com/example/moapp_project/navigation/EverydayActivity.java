package com.example.moapp_project.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moapp_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EverydayActivity extends AppCompatActivity {

    private RecyclerView timeRecyclerView;
    private TimeRecyclerviewAdapter timeAdapter;
    private List<String> userList = new ArrayList<>();

    String Currentuserid;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everyday);

        mFirebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("selectedDate");


        timeRecyclerView = findViewById(R.id.time_recyclerview);

        // 사용자 정보를 가져오는 메서드 호출 또는 데이터 초기화
        getUserList();
    }

    // 사용자 정보를 가져오는 메서드 또는 데이터 초기화
    private void getUserList() {
        Currentuserid=mFirebaseAuth.getCurrentUser().getUid();
        Query query = databaseReference.child("calender").child(Currentuserid).child(selectedDate).child("time");
        Log.e("query",query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> timeList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Long timeValue = snapshot.getValue(Long.class);
                    String time = String.valueOf(timeValue);
                    timeList.add(time);
                }

                timeAdapter = new TimeRecyclerviewAdapter(timeList);
                timeRecyclerView.setLayoutManager(new LinearLayoutManager(EverydayActivity.this));
                timeRecyclerView.setAdapter(timeAdapter);
                // time 데이터가 모두 ArrayList에 저장되었으므로 필요한 처리를 수행할 수 있습니다.
                // 예를 들어, RecyclerView에 데이터를 표시하거나 다른 작업을 수행할 수 있습니다.
                // 여기서는 timeList를 활용하여 원하는 작업을 수행하시면 됩니다.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 읽기 작업이 실패한 경우 처리할 내용
            }
        });
    }
}
