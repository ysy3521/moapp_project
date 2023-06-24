package com.example.moapp_project;

import com.example.moapp_project.activity.Mission;

public class MemberInfo {

    private String email;
    private String password;
    private int score;
    private Mission mission;
    private String user_name;

    public MemberInfo() {
        // Default constructor required for Firebase
    }

    public MemberInfo(String email, String password,String user_name, int score,Mission mission){
        this.email= email;
        this.password=password;
        this.user_name=user_name;
        this.score=score;
        this.mission=mission;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScore(){return this.score;}
    public void setScore(int score){
        this.score=score;
    }

    public Mission getMission(){return this.mission;}
    public void setMission(Mission mission){
        this.mission=mission;
    }

    public void setUser_name(String user_name){
        this.user_name= user_name;
    }
    public String getUser_name(String user_name){
        return user_name;
    }


}
