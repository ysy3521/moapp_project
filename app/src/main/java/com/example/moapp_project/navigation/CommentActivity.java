package com.example.moapp_project.navigation;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.moapp_project.R;
import com.example.moapp_project.model.AlarmDTO;
import com.example.moapp_project.model.ContentDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    private String contentUid = null;
    private Button comment_btn_send;
    private EditText comment_edit_message;
    private RecyclerView comment_recyclerview;
    private CommentRecyclerviewAdapter adapter;
    private LinearLayoutManager layoutManager;
    ArrayList<ContentDTO.Comment> comments = new ArrayList<>();

    private String destinationUid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);



        contentUid = getIntent().getStringExtra("contentUid");
        destinationUid = getIntent().getStringExtra("destinationUid");

        comment_recyclerview = findViewById(R.id.comment_recyclerview);
        comment_btn_send = findViewById(R.id.comment_btn_send);
        comment_edit_message = findViewById(R.id.comment_edit_message);
        comment_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentDTO.Comment comment = new ContentDTO.Comment();
                comment.setUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                comment.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                comment.setComment(comment_edit_message.getText().toString());
                comment.setTimestamp(String.valueOf(System.currentTimeMillis()));
                commentAlarm(destinationUid,comment_edit_message.getText().toString());
                FirebaseFirestore.getInstance().collection("images").document(contentUid)
                        .collection("comments").document().set(comment);

                comment_edit_message.setText("");
            }
        });

        layoutManager = new LinearLayoutManager(getApplication(),LinearLayoutManager.VERTICAL,true);
        comment_recyclerview.setLayoutManager(layoutManager);
        adapter = new CommentRecyclerviewAdapter(comments);
        comment_recyclerview.setAdapter(adapter);


    }
    private void commentAlarm (String destinationUid, String message){
        AlarmDTO alarmDTO = new AlarmDTO();
        alarmDTO.setDestinationUid(destinationUid);
        alarmDTO.setUserId(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        alarmDTO.setKind(1);
        alarmDTO.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        alarmDTO.setTimestamp(String.valueOf(System.currentTimeMillis()));
        alarmDTO.setMessage(message);
        FirebaseFirestore.getInstance().collection("alarms").document().set(alarmDTO);

        String message1 = FirebaseAuth.getInstance().getCurrentUser().getEmail()+message;
        //FcmPush.fcmPush.sendMessage(destinationUid,"psw",message1);



    }
    public class CommentRecyclerviewAdapter extends RecyclerView.Adapter<CommentRecyclerviewAdapter.ViewHolder>{

        public CommentRecyclerviewAdapter(ArrayList<ContentDTO.Comment> comments){
            FirebaseFirestore.getInstance().collection("images").document(contentUid)
                    .collection("comments").orderBy("timestamp")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(value == null){
                                return;
                            }
                            comments.clear();
                            for(QueryDocumentSnapshot doc : value){
                                comments.add(doc.toObject(ContentDTO.Comment.class));
                            }
                            notifyDataSetChanged();
                        }
                    });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
            ViewHolder holder= new ViewHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            View view = holder.itemView;
            holder.commentviewitem_textview_comment.setText(comments.get(position).getComment());
            holder.commentviewitem_textview_profile.setText(comments.get(position).getUserId());

            FirebaseFirestore.getInstance().collection("profileImage").document(comments.get(position).getUid())
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
                                //    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "onEvent: "+str);
                                Log.e("TAG", "onEvent: "+value.getData());
                                // Map<String, Object> str = value.getData();
                                Glide.with(holder.itemView)
                                        .load(uri)
                                        .apply(RequestOptions.circleCropTransform())
                                        .into(holder.commentviewitem_imageview_profile);

                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView commentviewitem_imageview_profile;
            TextView commentviewitem_textview_profile;
            TextView commentviewitem_textview_comment;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.commentviewitem_imageview_profile = itemView.findViewById(R.id.commentviewitem_imageview_profile);
                this.commentviewitem_textview_profile = itemView.findViewById(R.id.commentviewitem_textview_profile);
                this.commentviewitem_textview_comment = itemView.findViewById(R.id.commentviewitem_textview_comment);
            }
        }
    }
}