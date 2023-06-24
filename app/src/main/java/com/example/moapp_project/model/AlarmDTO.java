package com.example.moapp_project.model;

public class AlarmDTO {
    private String destinationUid = null;
    private String userId = null;
    private String uid = null;
    private int kind ;
    private String message = null;
    private String timestamp = null;

    /*
    kind = 0 like alarm
    kind = 1 comment alarm
    kind = 2 follow alarm
    */
    public AlarmDTO() {
    }

    public String getDestinationUid() {
        return destinationUid;
    }

    public void setDestinationUid(String destinationUid) {
        this.destinationUid = destinationUid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
