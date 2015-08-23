package com.michaelho.watermonitor.objects;

/**
 * Created by Administrator on 2015/8/17.
 */
public class SingleMessage {

    private String title;
    private String detail;
    private String ifRead;
    private String sender;
    private String time;

    public SingleMessage() {

    }

    public SingleMessage(String title, String detail, String sender, String time, String ifRead) {
        this.title = title;
        this.detail = detail;
        this.ifRead = ifRead;
        this.sender = sender;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSender() {
        return sender;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getIfRead() {
        return ifRead;
    }

    public void setIfRead(String ifRead) {
        this.ifRead = ifRead;
    }
}
