package com.example.moapp_project.activity;

public class Mission {
    String mission_name; //미션 이름
    long mission_time; //미션 수락 시간 (getCurrentTime)
    String user_name; //올린 사람
    int heart_num; //하트 수
    int mission_can; //미션 수행 가능 여부

    int get_score; //이 미션으로 점수를 받은적이 있는지에 대한 정보

    public Mission(){} // 생성자 메서드


    //getter, setter 설정

    public int getGet_score(){return get_score;}
    public void setGet_score(int get_score){
        this.get_score=get_score;
    }
    public String getMission_name() {
        return mission_name;
    }
    public long getMission_time(){
        return mission_time;
    }
    public String getUser_name(){
        return user_name;
    }
    public int getHeart_num(){
        return heart_num;
    }
    public int getMission_can(){
        return mission_can;
    }

    public void setMission_name(String mission_name){
        this.mission_name= mission_name;
    }
    public void setUser_name(String user_name){
        this.user_name= user_name;
    }
    public void setMission_time(long mission_time){
        this.mission_time= mission_time;
    }
    public void setgetHeart_num(int heart_num){
        this.heart_num= heart_num;
    }
    public void setMission_can(int mission_can){
        this.mission_can=mission_can;
    }
    //값을 추가할때 쓰는 함수, MainActivity에서 addmision함수에서 사용할 것임.
    public Mission(String mission_name,long mission_time, String user_name, int heart_num, int mission_can, int get_score){
        this.mission_name = mission_name;
        this.mission_time = mission_time;
        this.user_name= user_name;
        this.heart_num= heart_num;
        this.mission_can=mission_can;
        this.get_score=get_score;
    }
}