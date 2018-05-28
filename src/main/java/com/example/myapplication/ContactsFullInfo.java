package com.example.myapplication;

/**
 * Created by TEARREAL on 2018/5/21.
 */

public class ContactsFullInfo {
    private String COUNT;
    private String name;
    private String phone;
    private String head;
    private String address;
    private String id;

    public ContactsFullInfo(String name, String phone, String head, String address) {
        this.name = name;
        this.phone = phone;
        this.head = head;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ContactsFullInfo(String COUNT, String name, String phone, String head, String address) {
        this.COUNT = COUNT;
        this.name = name;
        this.phone = phone;
        this.head = head;
        this.address = address;
    }

    public ContactsFullInfo(String COUNT, String name, String phone, String head, String address, String id) {
        this.COUNT = COUNT;
        this.name = name;
        this.phone = phone;
        this.head = head;
        this.address = address;
        this.id = id;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
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
