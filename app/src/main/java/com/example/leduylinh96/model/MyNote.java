package com.example.leduylinh96.model;

import java.io.Serializable;

/**
 * Created by leduylinh96 on 4/28/2017.
 */

public class MyNote implements Serializable {
    private long id;
    private String title;
    private String note;
    private String time;
    private String date;
    private String dayCreated;
    private int color;
    private int alarm;
    private String imagesUri;

    public MyNote(String title, String note, String date, String time, String dayCreated, int color, String imagesUri, int alarm, long id) {
        this.title = title;
        this.note = note;
        this.time = time;
        this.date = date;
        this.dayCreated = dayCreated;
        this.color = color;
        this.imagesUri = imagesUri;
        this.alarm = alarm;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getDayCreated() {
        return dayCreated;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getColor() {
        return color;
    }

    public String getImagesUri() {
        return imagesUri;
    }

    public int getAlarm() {
        return alarm;
    }
}
