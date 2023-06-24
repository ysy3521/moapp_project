package com.example.moapp_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moapp_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StopwatchActivity extends AppCompatActivity {
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset, getPauseOffset;
    private final long finishtimeed = 1000;
    private long presstime = 0;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth mFirebaseAuth;
    String date;

    private String currentUserUid= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stopwatch_layout);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //화면 꺼짐 방지
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        Button startBtn = findViewById(R.id.start_btn);
        Button stopBtn = findViewById(R.id.stop_btn);
        Button resetBtn = findViewById(R.id.reset_btn);

        //시작
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });
        //중지
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });
        //저장하기
        //저장하기
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running) { // 시작 버튼이 눌리지 않은 상태에서만 저장하도록 수정
                    Toast.makeText(getApplicationContext(), "시간이 측정되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                Log.e("getBase값", Long.toString(chronometer.getBase()));
                chronometer.setBase(SystemClock.elapsedRealtime());
                getPauseOffset = pauseOffset;

                // 걸린 시간 알림(text 변형해서 사용)
                if(getPauseOffset!=0){
                Toast.makeText(getApplicationContext(), "집중 시간이 저장되었습니다.", Toast.LENGTH_SHORT).show();}
                currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
                date = getTime();
                date = date.replace("-", "");
                addtime(getPauseOffset, currentUserUid, date);

                pauseOffset = 0;
                running = false;
                //캘린더로 화면 전환-나중에 캘린더 액티비티로 변경해서 사용하세요
                //매니패스트에 DayActivity 등록한 것 캘린더 액티비티로 변경!!
            }
        });

    }

//    @Override //한번 눌렀을 때 눌렀음을 경고
//    public boolean onKeyDown(int keycode, KeyEvent event) {
//        if(keycode ==KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(this, "뒤로가기버튼이 눌렸습니다",Toast.LENGTH_LONG).show();
//            return true;
//        }
//
//        return false;
//    }

    //모앱에 함수 이미 존재함(이건 임시로 생성)-기존 액티비티 무삭제 버전(뒤로가기 하면 이전 액티비티 나타남)
    private void myStartActivity_nonremove(Class c) {

        Intent intent = new Intent(this, c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*protected void onPause() {
        super.onPause();
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
        chronometer.setBase(SystemClock.elapsedRealtime());
        getPauseOffset = pauseOffset;
        // 걸린 시간 알림(text 변형해서 사용)
        currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
        date= getTime();
        date=date.replace("-","");
        addtime(getPauseOffset,currentUserUid,date);
        Toast.makeText(getApplicationContext(), "시간 확인: " + getPauseOffset, Toast.LENGTH_LONG).show();

        finish();

    }*/
    @Override
    protected void onStop() {
        super.onStop();
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
        getPauseOffset = pauseOffset;
        // 걸린 시간 알림(text 변형해서 사용)
        currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
        date = getTime();
        date = date.replace("-", "");
        addtime(getPauseOffset, currentUserUid, date);
        if(getPauseOffset!=0){
            Toast.makeText(getApplicationContext(), "집중 시간이 저장되었습니다.", Toast.LENGTH_SHORT).show();}
        finish();
    }


    /*@Override
    public boolean onKeyDown(int keycode, KeyEvent event) {//홈버튼 눌렀을때 종료
        if (keycode == KeyEvent.KEYCODE_HOME) {
            if (running) {
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                running = false;
            }
            chronometer.setBase(SystemClock.elapsedRealtime());
            getPauseOffset = pauseOffset;
            // 걸린 시간 알림(text 변형해서 사용)
            currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
            date= getTime();
            date=date.replace("-","");
            //addtime(getPauseOffset,currentUserUid,date);
            Toast.makeText(getApplicationContext(), "시간 확인: " + getPauseOffset, Toast.LENGTH_LONG).show();
            finish();
            return true; // 이벤트를 소비하여 다른 동작이 발생하지 않도록 함
        }


        else if (keycode == KeyEvent.KEYCODE_BACK) { // 뒤로 가기 눌렀을때 종료
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - presstime;

            if (0 < intervalTime && finishtimeed >= intervalTime) { // 130~133은 73번쩨줄에서 가져옴
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
                chronometer.setBase(SystemClock.elapsedRealtime());
                getPauseOffset = pauseOffset; // 걸린 시간 저장 받기: 1000 == 1초

                // 걸린 시간 알림(text 변형해서 사용)
                currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
                date= getTime();
                date=date.replace("-","");
                addtime(getPauseOffset,currentUserUid,date);
                Toast.makeText(getApplicationContext(), "시간 확인: " + getPauseOffset, Toast.LENGTH_LONG).show();

                finish();
            } else { // 그냥 2번터치 넣고 싶어서 넣은거라 빼도 됨
                presstime = tempTime;
                Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }
*/
    public void addtime(long getPauseOffset, String user_name, String date) {
        String sanitizedUserName = user_name.replace(".", ",");
        String sanitizedDate = date.replace(".", ",");

        if(getPauseOffset==0){
            return;
        }

        databaseReference.child("calender").child(sanitizedUserName).child(sanitizedDate).child("time").push().setValue(getPauseOffset);
        Toast.makeText(getApplicationContext(), "집중 시간이 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = dateFormat.format(date);

        return getTime;
    }



}