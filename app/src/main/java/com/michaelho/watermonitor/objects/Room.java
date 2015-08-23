package com.michaelho.watermonitor.objects;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Room {
    private String name;
    private String waterData;
    private String floor;
    private String timeDate;
    private String time;

    public Room() {

    }

    public Room(String name, String waterData, String floor, String timeDate, String time) {
        this.name = name;
        this.waterData = waterData;
        this.floor = floor;
        this.timeDate = timeDate;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWaterData() {
        return waterData;
    }

    public void setWaterData(String waterData) {
        this.waterData = waterData;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}