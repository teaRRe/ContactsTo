package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TEARREAL on 2018/5/20.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CONTACTS = "create table contacts (" +
            "id integer primary key autoincrement, " +
            "name text, " +
            "phone text, " +
            "address text, " +
            "head text, " +
            "count int)";
    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS);
        Toast.makeText(mContext, "Database Create successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists contacts");
        onCreate(db);
    }
    public ContactsFullInfo getContactFullInoByNameAndCount(String _name, String _count){
        //SQLiteDatabase db = new DatabaseHelper(context, "Contacts.db", null, 2).getWritableDatabase();

        SQLiteDatabase db = this.getWritableDatabase();
        ContactsFullInfo contact = new ContactsFullInfo();
        String queryAllSql = "select * from contacts where name = ? and count = ?;";

        Cursor cursor = db.rawQuery(queryAllSql, new String[]{_name, _count});
        if (cursor.moveToFirst()){
            do {
                String CID = cursor.getString(cursor.getColumnIndex("id"));
                String head = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));

                contact = new ContactsFullInfo(name, phone, head, address);
                contact.setCID(CID);

            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return contact;
    }
    public int userCount(String _name){
        int count = 1;
        SQLiteDatabase db = this.getWritableDatabase();
        String queryAllSql = "select * from contacts where name = ?;";
        Cursor cursor = db.rawQuery(queryAllSql, new String[]{_name});
        if (cursor.moveToFirst()){
            do {
                count++;
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return count;
    }

}
