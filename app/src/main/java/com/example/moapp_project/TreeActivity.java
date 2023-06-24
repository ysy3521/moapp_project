package com.example.moapp_project;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TreeActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth mFirebaseAuth;

    String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tree_activity);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserUid=mFirebaseAuth.getCurrentUser().getUid();

        DatabaseReference scoreRef = databaseReference.child("user").child(currentUserUid).child("score");
        scoreRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int score = dataSnapshot.getValue(Integer.class);

                    switch(score/10){
                        case 0:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree1);
                            break;
                        case 1:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree2);
                            break;
                        case 2:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree3);
                            break;
                        case 3:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree4);
                            break;
                        case 4:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree5);
                            break;
                        case 5:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree6);
                            break;
                        case 6:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree7);
                            break;
                        case 7:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree8);
                            break;
                        case 8:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree9);
                            break;
                        case 9:
                            ((ImageView) findViewById(R.id.tree_image)).setImageResource(R.drawable.tree10);
                            break;
                }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 처리 중 오류가 발생한 경우의 동작 처리
            }
        });

    }

}