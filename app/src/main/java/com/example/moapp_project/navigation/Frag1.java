package com.example.moapp_project.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moapp_project.R;
import com.example.moapp_project.model.ContentDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Frag1 extends Fragment {

    private View view;
    private FirebaseFirestore firestore;
    //리싸이클러뷰 사용을 위한 선언↓
    private RecyclerView detailviewfragment_recyclerview;
    private DetailAdapter adapter;
    private LinearLayoutManager layoutManager;
    ArrayList<ContentDTO> contentDTOs = new ArrayList<>();
    ArrayList<String> contentUidList = new ArrayList<>();
    String uid;

    private ImageView detailviewitem_favrite_imageview;




    //2번
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail,container,false);

        firestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        //리싸이클러뷰를 정의한다.↓
        detailviewfragment_recyclerview = view.findViewById(R.id.detailviewfragment_recyclerview);
        detailviewfragment_recyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,true);
        layoutManager.setStackFromEnd(true);
        detailviewfragment_recyclerview.setLayoutManager(layoutManager);
        //adapter에 contentDTOs를 담아서
        //리싸이클러뷰에 적용 시킨다.
        adapter = new DetailAdapter(getActivity(),contentDTOs);
        detailviewfragment_recyclerview.setAdapter(adapter);
        detailviewitem_favrite_imageview = view.findViewById(R.id.detailviewitem_favrite_imageview);


        return view;
    }


}
