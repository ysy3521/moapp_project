package com.example.moapp_project.navigation;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moapp_project.R;
import com.example.moapp_project.model.AlarmDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//AlarmFragment
public class Frag4 extends Fragment {
    private View view;
    private RecyclerView alarmfragment_recyclerview;
    private AlarmRecyclerviewAdapter adapter;
    private LinearLayoutManager layoutManager;
    ArrayList<AlarmDTO> alarmDTOList = new ArrayList<>();
    String str_0,str_1,str_2,str_message = null;

    private FirebaseAuth mFirebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag4,container,false);
        alarmfragment_recyclerview = view.findViewById(R.id.alarmfragment_recyclerview);

        //layoutManager는 리사이클러뷰의 방향을 정함
        layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        alarmfragment_recyclerview.setLayoutManager(layoutManager);
        adapter = new AlarmRecyclerviewAdapter(alarmDTOList);
        alarmfragment_recyclerview.setAdapter(adapter);


        return view;
    }

    public class AlarmRecyclerviewAdapter extends RecyclerView.Adapter<AlarmRecyclerviewAdapter.Holder>{
        public AlarmRecyclerviewAdapter(ArrayList<AlarmDTO> alarmDTOList){
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.e("TAGtest", "onEvent:" + uid+"test");
            FirebaseFirestore.getInstance().collection("alarms").whereEqualTo("destinationUid",uid)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            alarmDTOList.clear();
                            if (value == null){
                                Log.e("TAG", "onEvent: null");
                                return;
                            }
                            for(QueryDocumentSnapshot doc : value){
                                alarmDTOList.add(doc.toObject(AlarmDTO.class));
                                Log.e("TAG", "onEvent: 데이터 가져옴");
                            }
                            Collections.sort(alarmDTOList,new SortByDate());
                            Collections.reverse(alarmDTOList);
                            notifyDataSetChanged();
                        }
                    });
        }

        @NonNull
        @Override
        public AlarmRecyclerviewAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
            Holder holder = new Holder(view);


            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull AlarmRecyclerviewAdapter.Holder holder, int position) {
            FirebaseFirestore.getInstance().collection("profileImage").document(alarmDTOList.get(position).getUid())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            try{
                                if(value.getData()==null){

                                    return;
                                }
                            }catch (Exception e){

                            }

                            if(value !=null ){
                                String str = String.valueOf(value.getData());
                                str=str.substring(10,str.length()-1);
                                Uri uri = Uri.parse(str);
                                Log.e("TAG", "onEvent: "+str);
                                Log.e("TAG", "onEvent: "+value.getData());
                                Glide.with(holder.itemView)
                                        .load(uri)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(holder.commentviewitem_imageview_profile);

                            }
                        }
                    });

            switch (alarmDTOList.get(position).getKind()){
                case 0:
                    //좋아요
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    str_0 = alarmDTOList.get(position).getUserId();
                    holder.commentviewitem_textview_profile.setText(str_0);
                    holder.commentviewitem_textview_comment.setText("님이 좋아요를 눌렀어요");
                    /////내가 건드린 부분

                    Log.e("TAG0", "onBindViewHolder: "+str_0);
                    break;

                case 1:
                    //댓글
                    str_1 = alarmDTOList.get(position).getUserId();
                    str_message = alarmDTOList.get(position).getMessage();
                    holder.commentviewitem_textview_profile.setText(str_1);
                    holder.commentviewitem_textview_comment.setText(str_message);
                    Log.e("TAG1", "onBindViewHolder: "+str_1);
                    break;

                case 2:
                    //팔로우
                    str_2 = alarmDTOList.get(position).getUserId();
                    holder.commentviewitem_textview_profile.setText(str_2);
                    Log.e("TAG2", "onBindViewHolder: "+str_2);
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return alarmDTOList.size();
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView commentviewitem_imageview_profile;
            TextView commentviewitem_textview_profile;
            TextView commentviewitem_textview_comment;
            public Holder(@NonNull View itemView) {
                super(itemView);
                this.commentviewitem_imageview_profile = itemView.findViewById(R.id.commentviewitem_imageview_profile);
                this.commentviewitem_textview_profile = itemView.findViewById(R.id.commentviewitem_textview_profile);
                this.commentviewitem_textview_comment = itemView.findViewById(R.id.commentviewitem_textview_comment);
            }
        }
    }
    class SortByDate implements Comparator<AlarmDTO> {
        @Override
        public int compare(AlarmDTO alarmDTO, AlarmDTO t1) {
            return alarmDTO.getTimestamp().compareTo(t1.getTimestamp());
        }
    }
}

