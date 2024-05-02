package com.affc.ait.models;

public class Course {

    private int course_ID;
    private String course_name;
    private String start_date;
    private String end_date;
    private double fee;
    private String description;
    private int max_p;

    public Course(int id, String course_name, String start_date, String end_date, double fee, String description, int max_p) {
        this.course_ID = id;
        this.course_name = course_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.fee = fee;
        this.description = description;
        this.max_p = max_p;
    }

    public Course( String course_name, String start_date, String end_date, double fee, String description, int max_p) {
        this.course_name = course_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.fee = fee;
        this.description = description;
        this.max_p = max_p;
    }

    public int getCourse_ID() {
        return course_ID;
    }

    public void setCourse_ID(int course_ID) {
        this.course_ID = course_ID;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMax_p() {
        return max_p;
    }

    public void setMax_p(int max_p) {
        this.max_p = max_p;
    }

    @Override
    public String toString() {
        return "Course{" +
                "course_ID='" + course_ID + '\'' +
                ", course_name='" + course_name + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", fee=" + fee +
                ", description='" + description + '\'' +
                ", max_p=" + max_p +
                '}';
    }
}
