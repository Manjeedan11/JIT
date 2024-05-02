package com.affc.ait.models;

public class Branch {

    private int branch_id;
    private String branch_name;
    private String location;

    public Branch(int branch_id, String branch_name, String location) {
        this.branch_id = branch_id;
        this.branch_name = branch_name;
        this.location = location;
    }

    public Branch(String branch_name, String location) {
        this.branch_name = branch_name;
        this.location = location;
    }


    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
