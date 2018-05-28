package com.example.myapplication;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
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

    /**
     *根据name和count查找数据
     * @param _name
     * @param _count
     * @return
     */
    public ContactsFullInfo getContactFullInoByNameAndCount(String _name, String _count){
        //SQLiteDatabase db = new DatabaseHelper(context, "Contacts.db", null, 2).getWritableDatabase();

        SQLiteDatabase db = this.getWritableDatabase();
        ContactsFullInfo contact = new ContactsFullInfo();
        String queryAllSql = "select * from contacts where name = ? and count = ?;";

        Cursor cursor = db.rawQuery(queryAllSql, new String[]{_name, _count});
        if (cursor.moveToFirst()){
            do {
                String COUNT = cursor.getString(cursor.getColumnIndex("count"));
                String head = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String id = cursor.getString(cursor.getColumnIndex("id"));

                contact = new ContactsFullInfo(name, phone, head, address);

            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return contact;
    }

    /**
     *根据唯一id查找数据
     * @param _id
     * @return
     */
    public ContactsFullInfo getFullInfoById(String _id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContactsFullInfo contact = new ContactsFullInfo();
        String querySql = "select * from contacts where id = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[]{_id});
        if (cursor.moveToFirst()){
            do {
                String COUNT = cursor.getString(cursor.getColumnIndex("count"));
                String head = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));

                contact = new ContactsFullInfo(COUNT, name, phone, head, address);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return contact;
    }

    /**
     * 根据name查询数据库里是否已经有了相同name的数据
     * 如果有，计算数量并返回
     * @param _name
     * @return
     */
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

    /**
     * 根据id删除一条数据
     * @param id
     */
    public void onDelete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteSql = "delete from contacts where id = ?;";
        db.execSQL(deleteSql, new String[]{id});
    }
    /**
     * 添加一条数据
     * @param contactsFullInfo
     */
    public void onAdd(ContactsFullInfo contactsFullInfo){

        SQLiteDatabase db = this.getWritableDatabase();
        int count = this.userCount(contactsFullInfo.getName());
        ContentValues values = new ContentValues();
        values.put("name", contactsFullInfo.getName());
        values.put("phone", contactsFullInfo.getPhone());
        values.put("address", contactsFullInfo.getAddress());
        values.put("count", count);

        db.insert("contacts", null, values);
        values.clear();
    }
    /**
     * 更新数据
     * @param id
     * @param contacts
     */
    public void onUpdate(String id, ContactsFullInfo contacts){
        SQLiteDatabase db = this.getWritableDatabase();
        String updateSql = "update contacts set phone=?, address=?,head=?, name=? where id=?;";
        db.execSQL(updateSql, new String[]{contacts.getPhone(), contacts.getAddress(), contacts.getHead(), contacts.getName(), id});

    }

    /**
     * 删除数据以后，count需要重新计算
     * @param count
     * @param name
     */
    public void onUpdateAfterDelete(String id, String count, String name){

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "update contacts set count=? where name=? and id = ?;";
        db.execSQL(sql, new String[] {  count, name, id});

    }

    /**
     * 通过name找出数据集合
     * @param _name
     * @return
     */
    public List<ContactsFullInfo> selectByName(String _name){
        List<ContactsFullInfo> list = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        String querySql = "select * from contacts where name = ?;";
        Cursor cursor = db.rawQuery(querySql, new String[]{_name});
        if (cursor.moveToFirst()){
            do {
                String COUNT = cursor.getString(cursor.getColumnIndex("count"));
                String head = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                ContactsFullInfo contactsFullInfo = new ContactsFullInfo(COUNT, name, phone, head, address, id);
                list.add(contactsFullInfo);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * 获取主界面显示的数据。。。。。
     * @param context
     * @return
     */
    public List<ContactsItem> getDate(Context context){
        List<ContactsItem> list = new ArrayList<>();

        SQLiteDatabase db = new DatabaseHelper(context, "Contacts.db", null, 3).getWritableDatabase();

        String quertAllSql = "select * from contacts;";
        Cursor cursor = db.rawQuery(quertAllSql, null);
        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String img = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String COUNT = cursor.getString(cursor.getColumnIndex("count"));
                /**
                 * 设置默认的头像
                 */
                if (null == img || "".equals(img))
                    img = getUriFromDrawableRes(context, R.drawable.ic_nature_people_48pt_3x);

                ContactsItem item = new ContactsItem(id, COUNT, img, name);
                list.add(item);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * 把drawable里面的数据转成Uri可以解析的String。
     * @param context
     * @param id
     * @return
     */
    public String getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        //Log.i("Log", "path: " + path);
        return path;
    }

    /**
     *
     * @param context
     * @param _name
     */
    public List<ContactsItem> onFindByName(Context context, String _name){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from contacts where name like ?";
        List<ContactsItem> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(sql, new String[] { "%" + _name + "%"});
        if (cursor.moveToFirst()){
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String img = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String COUNT = cursor.getString(cursor.getColumnIndex("count"));
                /**
                 * 设置默认的头像
                 */
                if (null == img || "".equals(img))
                    img = getUriFromDrawableRes(context, R.drawable.ic_nature_people_48pt_3x);

                ContactsItem item = new ContactsItem(id, COUNT, img, name);
                list.add(item);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

}
