package com.zxl.river.chief.http.data;

/**
 * Created by mac on 17-11-24.
 */

public class EventData {

    private String title = "";
    private String uploadEventTime = "";
    private int eventType = 0;
    private String uploadEventName = "";
    private String uploadEventDepartment = "";
    private int eventState = 0;
    private String dealEventTime = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUploadEventTime() {
        return uploadEventTime;
    }

    public void setUploadEventTime(String uploadEventTime) {
        this.uploadEventTime = uploadEventTime;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getUploadEventName() {
        return uploadEventName;
    }

    public void setUploadEventName(String uploadEventName) {
        this.uploadEventName = uploadEventName;
    }

    public String getUploadEventDepartment() {
        return uploadEventDepartment;
    }

    public void setUploadEventDepartment(String uploadEventDepartment) {
        this.uploadEventDepartment = uploadEventDepartment;
    }

    public int getEventState() {
        return eventState;
    }

    public void setEventState(int eventState) {
        this.eventState = eventState;
    }

    public String getDealEventTime() {
        return dealEventTime;
    }

    public void setDealEventTime(String dealEventTime) {
        this.dealEventTime = dealEventTime;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "title='" + title + '\'' +
                ", uploadEventTime='" + uploadEventTime + '\'' +
                ", eventType=" + eventType +
                ", uploadEventName='" + uploadEventName + '\'' +
                ", uploadEventDepartment='" + uploadEventDepartment + '\'' +
                ", eventState=" + eventState +
                ", dealEventTime='" + dealEventTime + '\'' +
                '}';
    }
}
