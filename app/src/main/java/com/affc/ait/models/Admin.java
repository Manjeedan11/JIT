package com.affc.ait.models;

public class Admin {

    private int admin_ID;
    private String admin_email;
    private String admin_password;

    public Admin(int admin_ID, String admin_email, String admin_password) {
        this.admin_ID = admin_ID;
        this.admin_email = admin_email;
        this.admin_password = admin_password;
    }

    public int getAdmin_ID() {
        return admin_ID;
    }

    public void setAdmin_ID(int admin_ID) {
        this.admin_ID = admin_ID;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "admin_ID=" + admin_ID +
                ", admin_email='" + admin_email + '\'' +
                ", admin_password='" + admin_password + '\'' +
                '}';
    }

    public String hashPassword() {
        //TODO
        return "stub";
    }

}
