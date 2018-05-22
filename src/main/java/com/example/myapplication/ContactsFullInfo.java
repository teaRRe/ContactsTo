package com.example.myapplication;

/**
 * Created by TEARREAL on 2018/5/21.
 */

public class ContactsFullInfo {
    private String CID;
    private String name;
    private String phone;
    private String head;
    private String address;

    public ContactsFullInfo(String name, String phone, String head, String address) {
        this.name = name;
        this.phone = phone;
        this.head = head;
        this.address = address;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCID() {
        return CID;
    }

    public ContactsFullInfo() {
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getHead() {
        return head;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
