package com.example.moapp_project.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moapp_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

    public class RandomMissionActivity extends AppCompatActivity {
        private TextView TV;
        private ImageButton btn_randm;
        private Button btn_admit;
        private String items[] = {"창문열기", "콘센트 빼기", "방밖으로 나가기", "설거지하기", "손씻기", "손톱 깎기", "빨래 개기", "세수하기", "핸드로션 바르기", "물 마시기", "책상 정리하기", "기지개 켜기", "가방 정리하기", "쓰레기 버리기", "이불 개기", "신발장 정리하기", "옷장 정리하기", "신발끈 다시 묶기", "셀카 찍기", "화장실 가서 눈꼽 떼기", "핸드폰 충전하기", "머리 빗기", "양치하기", "안경 닦기", "캘린더 확인해보기", "할 일 정리하기", "간식 먹기", "빨래 돌리기", "부모님께 전화 걸기", "돌돌이(청소기) 돌리기", "분리수거하기", "코끼리코 돌기"};
        private String currentUserUid = null;

        private FirebaseDatabase database = FirebaseDatabase.getInstance();
        private DatabaseReference databaseReference = database.getReference();
        private FirebaseAuth mFirebaseAuth;

        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String date;
        private int cnt=3;
        private TextView remainingCountTextView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            mFirebaseAuth=FirebaseAuth.getInstance();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_randompage);

            TV = findViewById(R.id.tv);
            btn_randm = findViewById(R.id.imageButton2);
            remainingCountTextView = findViewById(R.id.textView);
            btn_admit=findViewById(R.id.button);

            btn_randm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cnt--;
                    if (cnt == 2) {
                        btn_randm.setImageResource(R.drawable.box_open);
                        updateRemainingCountText();
                    }
                    else if (cnt == 1) {
                        btn_randm.setImageResource(R.drawable.box_open);
                        updateRemainingCountText();
                    }
                    else{
                        btn_randm.setImageResource(R.drawable.box_close);
                        updateRemainingCountText();
                        btn_randm.setEnabled(false);
                    }
                    Random rndm = new Random();
                    int i = rndm.nextInt(items.length);

                    TV.setText(items[i]);
                }
            });

            btn_admit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //에딧 텍스트 값을 문자열로 바꾸어 함수에 넣어줍니다.
                    currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
                    mNow = System.currentTimeMillis();
                    addmission(TV.getText().toString(),mNow,currentUserUid.toString(),0,1,0);
                    Mission new_mission = new Mission(TV.getText().toString(),mNow,currentUserUid.toString(),0,1,0);
                    databaseReference.child("user").child(currentUserUid).child("mission").setValue(new_mission);
                    Toast.makeText(RandomMissionActivity.this, "미션 저장, 10분 내로 수행하세요!", Toast.LENGTH_SHORT).show();

                }
            });

            }

        @SuppressLint("SetTextI18n")
        private void updateRemainingCountText() {
            remainingCountTextView.setText("남은 횟수: " + cnt);
        }

        private String getTime(){
            mNow = System.currentTimeMillis();
            mDate = new Date(mNow);
            return mFormat.format(mDate);
        }
        private String getFormattedDate(long timeInMillis) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date(timeInMillis);
            return dateFormat.format(date);
        }


        //값을 파이어베이스 Realtime database로 넘기는 함수
        public void addmission (String mission_name,long mission_time, String user_name, int heart_num, int mission_can,int get_score){

            //여기에서 직접 변수를 만들어서 값을 직접 넣는것도 가능합니다.
            // ex) 갓 태어난 동물만 입력해서 int age=1; 등을 넣는 경우

            //mission.java에서 선언했던 함수.
            Mission new_mission = new Mission(mission_name, mission_time,user_name,heart_num,mission_can,get_score);
            //child는 해당 키 위치로 이동하는 함수입니다.
            //키가 없는데 "mission"와 name같이 값을 지정한 경우 자동으로 생성합니다.
            //값을 넣을때 상위 키값을 랜덤으로 지정하도록 push()로 설정한 모습입니다.
            //키값을 랜덤으로 넣고싶거나, 채팅과 같은 계속해서 추가되는 값에 사용하면 좋습니다.
            currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
            date= getFormattedDate(mission_time);

            databaseReference.child("mission").child(currentUserUid).child(date).push().setValue(new_mission);}

        }

