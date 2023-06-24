package com.example.moapp_project;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

    public class RandomMissionActivity extends AppCompatActivity {
        private TextView TV;
        private Button btn_randm;

        private String items[] = {"창문열기", "콘센트 빼기", "방밖으로 나가기", "설거지하기", "손씻기", "손톱 깎기", "빨래 개기", "세수하기", "핸드로션 바르기", "물 마시기", "책상 정리하기", "기지개 켜기", "가방 정리하기", "쓰레기 버리기", "이불 개기", "신발장 정리하기", "옷장 정리하기", "신발끈 다시 묶기", "셀카 찍기", "화장실 가서 눈꼽 떼기", "핸드폰 충전하기", "머리 빗기", "양치하기", "안경 닦기", "캘린더 확인해보기", "할 일 정리하기", "간식 먹기", "빨래 돌리기", "부모님께 전화 걸기", "돌돌이(청소기) 돌리기", "분리수거하기", "코끼리코 돌기"};

        private int cnt = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_randompage);

            TV = findViewById(R.id.tv);
            btn_randm = findViewById(R.id.btn_random);

            btn_randm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cnt++;
                    if (cnt == 1) {
                        Toast.makeText(RandomMissionActivity.this, "남은 횟수: " + (3 - cnt), Toast.LENGTH_SHORT).show();
                    }
                    else if (cnt == 2) {
                        Toast.makeText(RandomMissionActivity.this, "남은 횟수: " + (3 - cnt), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RandomMissionActivity.this, "남은 횟수: " + (3 - cnt), Toast.LENGTH_SHORT).show();
                        btn_randm.setBackgroundColor(Color.GRAY);
                        btn_randm.setEnabled(false);
                    }
                    Random rndm = new Random();
                    int i = rndm.nextInt(items.length);

                    TV.setText(items[i]);
                }
            });
        }
    }

