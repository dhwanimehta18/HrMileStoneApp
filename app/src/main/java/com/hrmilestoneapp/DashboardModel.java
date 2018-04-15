package com.hrmilestoneapp;

public class DashboardModel {
    String dashboardMessages;
    String senderId;
    String senderName;
    String date;
    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDashboardMessages() {
        return dashboardMessages;
    }

    public void setDashboardMessages(String dashboardMessages) {
        this.dashboardMessages = dashboardMessages;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
