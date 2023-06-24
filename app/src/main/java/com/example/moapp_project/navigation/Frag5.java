package com.example.moapp_project.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moapp_project.ProovingActivity;
import com.example.moapp_project.R;
import com.example.moapp_project.TreeActivity;
import com.example.moapp_project.model.ContentDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

// User Fragment
public class Frag5 extends Fragment {
    private View view;
    private FirebaseFirestore firestore;
    private FirebaseAuth mFirebaseAuth;
    private String currentUserUid = null;
    private View fragmentView;
    private Button account_btn_follow_signout;
    private ImageView account_iv_profile;
    private TextView account_tv_following_count;
    private TextView account_tv_follower_count;

    private RecyclerView account_recyclerview;
    private UserFragmentRecyclerviewAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    ArrayList<ContentDTO> contentDTOs = new ArrayList<>();
    TextView account_tv_post_count;

    public static String str;
    public static int PICK_PROFILE_FROM_ALBUM = 10;
    ProovingActivity proovingActivity;

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://pswinstagram-cbae2.appspot.com/");

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        proovingActivity = (ProovingActivity) getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_user, container, false);
        firestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        account_btn_follow_signout = fragmentView.findViewById(R.id.account_btn_follow_signout);
        account_iv_profile = fragmentView.findViewById(R.id.account_iv_profile);
        account_recyclerview = fragmentView.findViewById(R.id.account_recyclerview);
        account_tv_post_count = fragmentView.findViewById(R.id.account_tv_post_count);

        //현재 로그인 되어 있는 아이디
        currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
        Log.e("TAG", "onCreateView: currentUserUid = " + currentUserUid);

        account_btn_follow_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TreeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        account_recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new UserFragmentRecyclerviewAdapter(contentDTOs);
        account_recyclerview.setAdapter(adapter);

        loadUserImages();

        return fragmentView;
    }

    private void loadUserImages() {
        firestore.collection("images")
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        contentDTOs.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            ContentDTO contentDTO = document.toObject(ContentDTO.class);
                            contentDTOs.add(contentDTO);
                        }
                        // 업로드된 시간에 따라 정렬
                        Collections.sort(contentDTOs, (o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
                        adapter.notifyDataSetChanged();

                        int postCount = contentDTOs.size();
                        account_tv_post_count.setText(String.valueOf(postCount));
                    } else {
                        Log.d("TAG", "Error getting user images: ", task.getException());
                    }
                });
    }


    // User Fragment RecyclerView Adapter
    private class UserFragmentRecyclerviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<ContentDTO> contentList;

        UserFragmentRecyclerviewAdapter(ArrayList<ContentDTO> contentList) {
            this.contentList = contentList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
            return new UserFragmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            UserFragmentViewHolder viewHolder = (UserFragmentViewHolder) holder;

            // 이미지 로드
            Glide.with(holder.itemView.getContext())
                    .load(contentList.get(position).getImageUrl())
                    .into(viewHolder.iv_post_image);
        }

        @Override
        public int getItemCount() {
            return contentList.size();
        }

        private class UserFragmentViewHolder extends RecyclerView.ViewHolder {
            ImageView iv_post_image;

            UserFragmentViewHolder(@NonNull View itemView) {
                super(itemView);
                iv_post_image = itemView.findViewById(R.id.post_image);
            }
        }
    }
}
