package com.example.moapp_project;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.moapp_project.navigation.AddPhotoActivity;
import com.example.moapp_project.navigation.Frag1;
import com.example.moapp_project.navigation.Frag2;
import com.example.moapp_project.navigation.Frag3;
import com.example.moapp_project.navigation.Frag4;
import com.example.moapp_project.navigation.Frag5;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProovingActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore firestore;
    //하단 네비게이션바
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Frag1 frag1;
    private Frag2 frag2;
    private Frag3 frag3;
    private Frag4 frag4;
    private Frag5 frag5;

    public TextView toolbar_username;
    public ImageView toolbar_btn_back;
    public ImageView toolbar_title_image;

    private Uri imageUri;

    private NotificationManager manager;
    private NotificationCompat.Builder builder;

    private String CHANNEL_ID = "channel1";
    private String CHANEL_NAME = "Channel1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prooving);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if(shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            Toast.makeText(ProovingActivity.this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                        }

                        requestPermissions(new String[]
                                {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                }

                switch (item.getItemId()) {
                    case R.id.action_home:
                        fragment = new Frag1();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                        break;
                    case R.id.action_search:
                        fragment = new Frag2();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                        break;
                    case R.id.action_add_photo:
                        fragment = new Frag3();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                        Intent intent = new Intent(ProovingActivity.this, AddPhotoActivity.class);
                        startActivity(intent);
                        // 원하는 동작 수행
                        break;
                    case R.id.action_favorite_alarm:
                        fragment = new Frag4();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                        break;
                    case R.id.action_account:
                        fragment= new Frag5();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                        break;
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
                    return true;
                } else {
                    return false;
                }
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);

        registerPushToken();
        //registerFollow();
       // getHashKey();
    }
    public void setFrag(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (n){
            case 0:
                ft.replace(R.id.main_content,frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_content,frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_content,frag3);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_content,frag4);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_content,frag5);
                ft.commit();
                break;
        }
    }

    public void registerPushToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("TOKEN_NONE", "registerPushToken: "+task.getException());
                        return;
                    }
                    Map<String,String> push_token  = new HashMap<>();
                    String token = task.getResult();
                    push_token.put("pushToken",token);
                    Log.e("TOKEN", "registerPushToken: "+token);
                    FirebaseFirestore.getInstance().collection("pushTokens")
                            .document(mFirebaseAuth.getUid()).set(push_token);
                });
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Frag5.PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK){
            imageUri = data.getData();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child("userProfileImages").child(uid);
            UploadTask uploadTask = storageRef.putFile(imageUri);
            Map<String, Object > map  = new HashMap<>();
            map.put(storageRef.getDownloadUrl().toString(),imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String img_uri = uri.toString();
                            ProfileImage profileimage = new ProfileImage();
                            profileimage.setImageUri(img_uri);
                            Toast.makeText(getApplication(),"성공",Toast.LENGTH_SHORT).show();
                            firestore.collection("profileImage").document(uid)
                                    .set(profileimage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            setResult(Activity.RESULT_OK);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProovingActivity.this,"다시 시도해주세요",Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }
                    });
                }
            });
        }
    }

    public void pushAlarm(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
            //하위 버전일 경우
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        //알림창 제목
        builder.setContentTitle("알림");
        //알림창 메시지
        builder.setContentText("알림 메시지");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_baseline_favorite_24);
        Notification notification = builder.build();
        //알림창 실행
        manager.notify(1,notification);
    }



    @Override
    protected void onStop() {
        super.onStop();
       // FcmPush.fcmPush.sendMessage("W02TxxCZp0UNpzhLtkPLdzI7hGw2","hi","hello");  오류나서 주석처리해둠!
        //    pushAlarm();
    }
}