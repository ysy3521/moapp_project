package com.example.moapp_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moapp_project.ProovingActivity;
import com.example.moapp_project.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        findViewById(R.id.random_mission).setOnClickListener(onClickListener);
        findViewById(R.id.upload_btn).setOnClickListener(onClickListener);
        findViewById(R.id.calender_btn).setOnClickListener(onClickListener);
        findViewById(R.id.stopwatch_btn).setOnClickListener(onClickListener);

    }


    View.OnClickListener onClickListener= new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(SignUpActivity.class);
                    break;
                case R.id.random_mission:
                    myStartActivity(RandomMissionActivity.class);
                    break;
                case R.id.upload_btn:
                    myStartActivity(ProovingActivity.class);
                    break;

                case R.id.calender_btn:
                    myStartActivity(CalenderActivity.class);
                    break;
                case R.id.stopwatch_btn:
                    myStartActivity(StopwatchActivity.class);
                    break;
            }
        }
    };

    private void myStartActivity(Class c){

        Intent intent= new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
