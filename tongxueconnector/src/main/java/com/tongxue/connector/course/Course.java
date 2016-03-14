package com.tongxue.connector.course;

/**
 * Created by newnius on 16-3-14.
 */
public abstract class Course {

    public abstract String getSource();

    public String getUrl() {
        return "";
    }

    public String getCourseName() {
        return "";
    }


    public String getCourseImage() {
        return "";
    }


    public String getDescription() {
        return "";
    }

    public int getHotRate() {
        return 0;
    }


    public String getDuration() {
        return "";
    }


}
