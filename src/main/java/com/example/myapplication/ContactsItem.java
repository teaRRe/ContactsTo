package com.example.myapplication;

/**
 * Created by TEARREAL on 2018/5/20.
 */

public class ContactsItem {
    private String id;
    private String COUNT;
    private String img;
    private String name;

    public ContactsItem() {
    }

    public ContactsItem(String id, String COUNT, String img, String name) {
        this.id = id;
        this.COUNT = COUNT;
        this.img = img;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }
}
