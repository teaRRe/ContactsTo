package com.example.myapplication;

/**
 * Created by TEARREAL on 2018/5/20.
 */

public class ContactsItem {
    private String CID;
    private String img;
    private String name;

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }
}
