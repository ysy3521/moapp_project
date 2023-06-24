package com.example.moapp_project.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moapp_project.R;
import com.example.moapp_project.navigation.EverydayActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalenderActivity extends AppCompatActivity {
    public String readDay = null;
    public String str = null;
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn, move_Btn, stopwatch_Btn;
    public TextView diaryTextView, textView2, textView3;
    public EditText contextEditText;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private FirebaseAuth mFirebaseAuth;

    private String currentUserUid;

    public String selectedDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calenderactivity_layout);
        calendarView = findViewById(R.id.calendarView);
        diaryTextView = findViewById(R.id.diaryTextView);
        save_Btn = findViewById(R.id.save_Btn);
        del_Btn = findViewById(R.id.del_Btn);
        cha_Btn = findViewById(R.id.cha_Btn);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        contextEditText = findViewById(R.id.contextEditText);
        move_Btn = findViewById(R.id.move_Btn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d", year, month + 1, dayOfMonth));
                selectedDateString = Integer.toString(year) + Integer.toString(month + 1) + Integer.toString(dayOfMonth);
                if (month + 1 < 10) {
                    selectedDateString = Integer.toString(year) + '0' + Integer.toString(month + 1) + Integer.toString(dayOfMonth);
                }
                if (dayOfMonth < 10) {
                    selectedDateString = Integer.toString(year) + Integer.toString(month + 1) + '0' + Integer.toString(dayOfMonth);
                }
                contextEditText.setText("");
                Toast.makeText(getApplicationContext(), "diaryTe", Toast.LENGTH_SHORT);
                checkDay(year, month, dayOfMonth);
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str = contextEditText.getText().toString();
                saveDiary();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });

        move_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalenderActivity.this, EverydayActivity.class);
                intent.putExtra("selectedDate", selectedDateString); // 선택한 날짜를 문자열로 전달
                startActivity(intent);
            }
        });


    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void checkDay(int cYear, int cMonth, int cDay) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserUid = mFirebaseAuth.getCurrentUser().getUid();
        ref.child("calender").child(currentUserUid).child(selectedDateString).child("todo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String content = dataSnapshot.getValue(String.class);
                    if (content != null && !content.equals("")) {
                        contextEditText.setVisibility(View.INVISIBLE);
                        textView2.setVisibility(View.VISIBLE);
                        textView2.setText(content);

                        save_Btn.setVisibility(View.INVISIBLE);
                        cha_Btn.setVisibility(View.VISIBLE);
                        del_Btn.setVisibility(View.VISIBLE);

                        cha_Btn.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                contextEditText.setVisibility(View.VISIBLE);
                                textView2.setVisibility(View.INVISIBLE);
                                contextEditText.setText(content);

                                save_Btn.setVisibility(View.VISIBLE);
                                cha_Btn.setVisibility(View.INVISIBLE);
                                del_Btn.setVisibility(View.INVISIBLE);

                                //textView2.setText(contextEditText.getText());
                            }

                        });

                        del_Btn.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                textView2.setVisibility(View.INVISIBLE);
                                contextEditText.setText("");
                                contextEditText.setVisibility(View.VISIBLE);
                                save_Btn.setVisibility(View.VISIBLE);
                                cha_Btn.setVisibility(View.INVISIBLE);
                                del_Btn.setVisibility(View.INVISIBLE);
                                removeDiary();
                            }
                        });
                        if (textView2.getText() == "")
                        {
                            textView2.setVisibility(View.INVISIBLE);
                            diaryTextView.setVisibility(View.VISIBLE);
                            save_Btn.setVisibility(View.VISIBLE);
                            cha_Btn.setVisibility(View.INVISIBLE);
                            del_Btn.setVisibility(View.INVISIBLE);
                            contextEditText.setVisibility(View.VISIBLE);
                        }



                    }
                } else {
                    // 해당 경로에 데이터가 없는 경우 처리
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 가져오기 실패 시 처리
            }
        });



    }




    @SuppressLint("WrongConstant")
    public void removeDiary()
    {
        String content = contextEditText.getText().toString();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserUid=mFirebaseAuth.getCurrentUser().getUid();
        databaseReference.child("calender").child(currentUserUid).child(selectedDateString).child("todo").setValue("");}


    @SuppressLint("WrongConstant")
    public void saveDiary()
    {
        String content = contextEditText.getText().toString();
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserUid=mFirebaseAuth.getCurrentUser().getUid();
        databaseReference.child("calender").child(currentUserUid).child(selectedDateString).child("todo").setValue(content);}


}



