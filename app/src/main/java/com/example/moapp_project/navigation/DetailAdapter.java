package com.example.moapp_project.navigation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moapp_project.R;
import com.example.moapp_project.activity.MainActivity;
import com.example.moapp_project.model.AlarmDTO;
import com.example.moapp_project.model.ContentDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder>{
    private FirebaseFirestore firestore;
    private Context context;
    private Activity activity;
    private ImageView detailviewitem_favrite_imageview;

    private String currentUserUid = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth mFirebaseAuth;

    int heart_num;
    int can;
    int score;


    ArrayList<ContentDTO> contentDTOs;
    public ArrayList<String> contentUidList = new ArrayList<>();

    String uid;

    int check=3;
    int get_point;

    Map<String,Boolean> favori  = new HashMap<>();


    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";



    //1번
    //context로 frag1에 넘겨 준다
    public DetailAdapter(Context context, ArrayList<ContentDTO> contentDTOs) {
        this.context = context;
        this.contentDTOs = contentDTOs;

        mFirebaseAuth= FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("images").orderBy("timestamp").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots==null)
                            return;
                        contentDTOs.clear();
                        contentUidList.clear();
                        try{
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                                ContentDTO item = snapshot.toObject(ContentDTO.class);
                                contentDTOs.add(item);
                                contentUidList.add(snapshot.getId());
                            }
                        }catch (Exception e){
                        }
                        notifyDataSetChanged();
                    }
                });

    }

    //3번
    //일반 onCreate랑 비슷한 친구
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,parent,false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }
    //4번
    //실제 추가 될때 작성하는 부분
    //이곳에서 변경되는 부분을 작성하면 다음 프래그1이 실행 될때 추가된다.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.detailviewitem_profile_textview.setText(contentDTOs.get(position).getUserId());
        Glide.with(holder.itemView)
                .load(contentDTOs.get(position).getImageUrl())
                .into(holder.detailviewitem_profile_imageview_content);
        holder.detailviewitem_explain_textview.setText(contentDTOs.get(position).getExplain());
        holder.detailviewitem_favoritecounter_textview.setText("Likes "+contentDTOs.get(position).getFavoriteCount());

        ///////////////////
        ////////

        FirebaseFirestore.getInstance().collection("profileImage").document(contentDTOs.get(position).getUid())
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
                            Glide.with(holder.itemView)
                                    .load(uri)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(holder.detailviewitem_profile_image);

                        }
                    }
                });

        if(contentDTOs.get(position).getFavorites().containsKey(uid)){
            holder.detailviewitem_favrite_imageview.setImageResource(R.drawable.ic_baseline_favorite_24);


        }else{
            holder.detailviewitem_favrite_imageview.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        holder.detailviewitem_profile_image.setTag(position);
        holder.detailviewitem_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frag5 fragment = new Frag5();
                Bundle bundle = new Bundle();
                bundle.putString("destinationUid",contentDTOs.get(position).getUid());
                bundle.putString("userId",contentDTOs.get(position).getUserId());
                fragment.setArguments(bundle);

                FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
                FragmentTransaction ft;
                ft = fm.beginTransaction();
                ft.replace(R.id.main_content,fragment);
                ft.commit();



            }
        });

        //좋아요 이벤트를 트렌잭션을 이용한 것으로 변경하면 좋을듯 하다.
        //팔로우 트렌잭션을 참고하자
        holder.detailviewitem_favrite_imageview.setTag(position);
        holder.detailviewitem_favrite_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favori.clear();
                favori.put(uid,true);

                DocumentReference tsDoc = firestore.collection("images").document(contentUidList.get(position));
                firestore.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        ContentDTO contentDTO =  transaction.get(tsDoc).toObject(ContentDTO.class);
                        if(contentDTO.getFavorites().containsKey(uid)){
                            contentDTO.setFavoriteCount(contentDTO.getFavoriteCount() -1);
                            contentDTO.getFavorites().remove(uid);

                            firestore.collection("images").whereEqualTo("favorites",favori)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value != null) {
                                                holder.detailviewitem_favrite_imageview.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                                holder.detailviewitem_favoritecounter_textview.setText("Likes "+contentDTO.getFavoriteCount());
                                                return;
                                            }

                                            for (DocumentSnapshot doc : value) {

                                            }
                                                notifyDataSetChanged();
                                        }
                                    });


                        }else{
                            favoriteAlarm(contentDTOs.get(position).getUid());
                            contentDTO.setFavoriteCount(contentDTO.getFavoriteCount() +1);
                            contentDTO.getFavorites().put(uid,true);
                            firestore.collection("images").whereEqualTo("favorites",favori)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (value != null) {
                                                holder.detailviewitem_favrite_imageview.setImageResource(R.drawable.ic_baseline_favorite_24);
                                                holder.detailviewitem_favoritecounter_textview.setText("Likes "+contentDTO.getFavoriteCount());
                                                return;
                                            }
                                            for (DocumentSnapshot doc : value) {

                                            }
                                                notifyDataSetChanged();
                                        }
                                    });

                        }
                        transaction.set(tsDoc,contentDTO);

                        return null;
                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return contentDTOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView detailviewitem_profile_image;
        TextView detailviewitem_profile_textview;
        ImageView detailviewitem_profile_imageview_content;
        TextView detailviewitem_favoritecounter_textview;
        TextView detailviewitem_explain_textview;
        ImageView detailviewitem_favrite_imageview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.detailviewitem_profile_image = itemView.findViewById(R.id.detailviewitem_profile_image);
            this.detailviewitem_profile_textview = itemView.findViewById(R.id.detailviewitem_profile_textview);
            this.detailviewitem_profile_imageview_content = itemView.findViewById(R.id.detailviewitem_profile_imageview_content);
            this.detailviewitem_favoritecounter_textview = itemView.findViewById(R.id.detailviewitem_favoritecounter_textview);
            this.detailviewitem_explain_textview = itemView.findViewById(R.id.detailviewitem_explain_textview);
            this.detailviewitem_favrite_imageview = itemView.findViewById(R.id.detailviewitem_favrite_imageview);
        }
    }
    private void favoriteAlarm(String destinationUid){
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setDestinationUid(destinationUid);
        alarmDTO.setUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        alarmDTO.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        alarmDTO.setKind(0);
        alarmDTO.setTimestamp(String.valueOf(System.currentTimeMillis()));
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO);

        String message = FirebaseAuth.getInstance().getCurrentUser().getEmail();
       // FcmPush.fcmPush.sendMessage(destinationUid,"psw",message);


    }

    /*
    참고용 이지만 완벽한 것은 아니다.
    public void favoriteEvent(int position) {
        DocumentReference tsDoc = firestore.collection("images").document(contentUidList.get(position));
        firestore.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                ContentDTO contentDTO = transaction.get(tsDoc).toObject(ContentDTO.class);

                if (contentDTO.getFavorites().containsKey(uid)) {
                    contentDTO.setFavoriteCount(contentDTO.getFavoriteCount() - 1);
                    contentDTO.getFavorites().remove(uid);

                    firestore.collection("images").whereEqualTo("favorites", favori)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value != null) {
                                        if (check == 1) {
                                            Toast.makeText(context.getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                                        } else if (check == 0) {
                                            Toast.makeText(context.getApplicationContext(), "0", Toast.LENGTH_SHORT).show();
                                            detailviewitem_favrite_imageview.setImageResource(R.drawable.ic_baseline_favorite_border_24);


                                        }
                                        return;
                                    }
                                    for (DocumentSnapshot doc : value) {
                                    }
                                    notifyDataSetChanged();
                                }
                            });
                } else {
                    contentDTO.setFavoriteCount(contentDTO.getFavoriteCount() + 1);
                    contentDTO.getFavorites().put(uid, true);
                }
                transaction.set(tsDoc, contentDTO);
                return null;
            }
        });
    }
    */


}
