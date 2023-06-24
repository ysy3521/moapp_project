package com.example.moapp_project.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moapp_project.MemberInfo;
import com.example.moapp_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Frag2 extends Fragment {
    private RecyclerView rankRecyclerView;
    private RankRecyclerviewAdapter adapter;
    private List<MemberInfo> userList= new ArrayList<>();

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        rankRecyclerView = view.findViewById(R.id.rank_recyclerview);

        // 사용자 정보를 가져오는 메서드 또는 데이터 초기화
        userList = getUserList();

        // 어댑터 초기화 및 설정
        adapter = new RankRecyclerviewAdapter(userList);
        rankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rankRecyclerView.setAdapter(adapter);

        return view;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 사용자 정보를 가져오는 메서드 호출 또는 데이터 초기화
        getUserList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        rankRecyclerView = view.findViewById(R.id.rank_recyclerview);

        return view;
    }

    // 사용자 정보를 가져오는 메서드 또는 데이터 초기화
    private void getUserList() {
        Query query = FirebaseDatabase.getInstance().getReference().child("user");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    MemberInfo user = userSnapshot.getValue(MemberInfo.class);
                    userList.add(user);
                }

                // 사용자 데이터가 모두 ArrayList에 저장되었으므로 어댑터 설정 및 RecyclerView 업데이트
                adapter = new RankRecyclerviewAdapter(userList);
                rankRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                rankRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 읽기 작업이 실패한 경우 처리할 내용
            }
        });
    }

}
