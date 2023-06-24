//package com.psw.pswinstagram.navigation.model;
//
//public class PushDTO {
//    private String to = null;
//    private Notification notification = new Notification();
//
//    public PushDTO() {
//    }
//
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public Notification getNotification() {
//        return notification;
//    }
//
//    public void setNotification(Notification notification) {
//        this.notification = notification;
//    }
//
//    public static class Notification{
//        private String bady = null;
//        private String title = null;
//
//        public Notification() {
//        }
//
//        public String getBady() {
//            return bady;
//        }
//
//        public void setBady(String bady) {
//            this.bady = bady;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//
//    }
//}


//package com.psw.pswinstagram.navigation.model;
//
//public class PushDTO {
//    private String to = null;
//
//    public PushDTO() {
//    }
//
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//    public static class Notification{
//        private String bady = null;
//        private String title = null;
//
//        public Notification() {
//        }
//
//        public String getBady() {
//            return bady;
//        }
//
//        public void setBady(String bady) {
//            this.bady = bady;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public void setTitle(String title) {
//            this.title = title;
//        }
//
//
//    }
//}
/*
                    String token = task.getResult().get("pushToken").toString();
                    PushDTO pushDTO = new PushDTO();
                    PushDTO.Notification notification = new PushDTO.Notification();
                    pushDTO.setTo(token);
                    notification.setTitle(title);
                    notification.setBady(message);
                    */


package com.example.moapp_project.model;

import android.app.Application;

public class PushDTO extends Application {
    private String to = null;
    private String bady = null;
    private String title = null;
//    NotificationManager manager;
//    NotificationCompat builder;


    public PushDTO() {
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBady() {
        return bady;
    }

    public void setBady(String bady) {
        this.bady = bady;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}


