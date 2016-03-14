package com.tongxue.connector.course;

/**
 * Created by newnius on 16-3-14.
 */
public class IMOOCCourse extends Course {
    private int id;
    private String name;
    private String pic;
    private String category;
    private int duration;
    private String duration_fmt;
    private String description;
    private int members;
    private long lastUpdate;

    public IMOOCCourse(int id, String name, String pic, String category, int duration, String duration_fmt, String description, int members, long lastUpdate) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.category = category;
        this.duration = duration;
        this.duration_fmt = duration_fmt;
        this.description = description;
        this.members = members;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String getSource() {
        return "www.imooc.com";
    }

    @Override
    public String getUrl() {
        return "http://www.imooc.com/learn/" + id;
    }

    @Override
    public String getCourseName() {
        return name;
    }

    @Override
    public String getCourseImage() {
        return "http://img.mukewang.com/" + pic + "-300-170.jpg";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getHotRate() {
        return members;
    }

    @Override
    public String getDuration() {
        return duration_fmt;
    }

}
