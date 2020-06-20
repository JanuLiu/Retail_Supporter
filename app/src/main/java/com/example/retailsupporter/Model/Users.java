package com.example.retailsupporter.Model;

import java.io.Serializable;
import java.util.HashMap;

public class Users implements Serializable {
    private HashMap<String, Object> userAuthority;
    private String userEmail;
    private String docDate;
    private HashMap<String, Object> groups;
    private HashMap<String, Object> workDays;
    private HashMap<String, Object> dailySales;

    //or some admin accounts can see more data?

    public Users() {}

    public Users(HashMap<String, Object> userAuthority, String userEmail, String docDate, HashMap<String, Object> groups, HashMap<String, Object> workDays, HashMap<String, Object> dailySales) {
        this.userAuthority = userAuthority;
        this.userEmail = userEmail;
        this.docDate = docDate;
        this.groups = groups;
        this.workDays = workDays;
        this.dailySales = dailySales;
    }

    public HashMap<String, Object> getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(HashMap<String, Object> userAuthority) {
        this.userAuthority = userAuthority;
    }

    public String getDocDate() {
        return docDate;
    }

    public void setDocDate(String docDate) {
        this.docDate = docDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public HashMap<String, Object> getGroups() {
        return groups;
    }

    public void setGroups(HashMap<String, Object> groups) {
        this.groups = groups;
    }

    public HashMap<String, Object> getWorkDays() {
        return workDays;
    }

    public void setWorkDays(HashMap<String, Object> workDays) {
        this.workDays = workDays;
    }

    public HashMap<String, Object> getDailySales() {
        return dailySales;
    }

    public void setDailySales(HashMap<String, Object> dailySales) {
        this.dailySales = dailySales;
    }
}
