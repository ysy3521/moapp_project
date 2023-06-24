package com.example.moapp_project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moapp_project.MemberInfo;
import com.example.moapp_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MemberInfo user;
    private String currentUserUid = null;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.SignUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    View.OnClickListener onClickListener= new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.SignUpButton:
                    signUp();
                    break;
                case R.id.gotoLoginButton:
                    myStartActivity(LoginActivity.class);
                    break;
            }
        }
    };

    private void signUp(){
        String email= ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck= ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        if(email.length()>0 && password.length()>0 && passwordCheck.length()>0) {
            if (password.equals(passwordCheck)) {


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    currentUserUid = mAuth.getCurrentUser().getUid();
                                    Mission basic = new Mission("시작 미션", 0,currentUserUid.toString(),0,0,0);
                                    adduser(email,password,currentUserUid.toString(),0,basic);
                                    startToast("성공적으로 회원가입 되었습니다.");
                                    myStartActivity(MainActivity.class);
                                    //UI updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.

                                    if (task.getException() != null) {
                                        startToast(task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        }
        else{
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void myStartActivity(Class c){

        Intent intent= new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //

    public void adduser (String email,String password,String user_name,int score,Mission mission){

        MemberInfo user = new MemberInfo(email, password,user_name, score,mission);
        databaseReference.child("user").child(user_name).setValue(user);

    }

}
