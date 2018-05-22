package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private List<ContactsItem> contactsItemList = new ArrayList<ContactsItem>();

    private Button btn_sech;
    private TextView txt_sech;
    private RecyclerView rec_sech;
    private RecyclerView rec_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        btn_sech = (Button) findViewById(R.id.btn_sech);
        txt_sech = (TextView) findViewById(R.id.txt_sech);
        rec_sech = (RecyclerView) findViewById(R.id.rec_sech);
        rec_show = (RecyclerView) findViewById(R.id.rec_show);


        btn_sech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txt_sech.getText() != "")
                    txt_sech.setText("");

                if (txt_sech.getVisibility() == View.GONE){
                    txt_sech.setVisibility(View.VISIBLE);
                    rec_sech.setVisibility(View.VISIBLE);
                    rec_show.setVisibility(View.GONE);
                    setTitle("搜索");
                }
                else {
                    txt_sech.setVisibility(View.GONE);
                    rec_sech.setVisibility(View.GONE);
                    rec_show.setVisibility(View.VISIBLE);
                    setTitle("ContactsTo");
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        fab.setImageResource(R.drawable.addpeople);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        //setData();
        getData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rec_show.setLayoutManager(layoutManager);
        rec_show.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        ContactsAdapter adapter = new ContactsAdapter(contactsItemList);
        rec_show.setAdapter(adapter);

    }


    /**
     * 从数据库里查询联系人
     */
    private void getData(){
        SQLiteDatabase db = new DatabaseHelper(this, "Contacts.db", null, 3).getWritableDatabase();

        String quertAllSql = "select * from contacts;";
        Cursor cursor = db.rawQuery(quertAllSql, null);
        if (cursor.moveToFirst()){
            do {
                String img = cursor.getString(cursor.getColumnIndex("head"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String CID = cursor.getString(cursor.getColumnIndex("count"));
                setDataFromDatabase("", name, CID);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
    }

    public String getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        Log.i("Log", "path: " + path);
        return path;
    }

    private void setDataFromDatabase(String img, String name, String CID){
        ContactsItem item = new ContactsItem();

        if (img == null || "".equals(img)){
            item.setImg(getUriFromDrawableRes(MainActivity.this, R.drawable.ic_nature_people_48pt_3x));
            item.setName(name);
            item.setCID("#" + CID);
        }else{
            item.setName(img);
            item.setName(name);
            item.setCID("#" + CID);
        }

        contactsItemList.add(item);
    }
    //测试数据
    private void setData(){

        for (int i = 0; i < 10; i++){
            ContactsItem item = new ContactsItem();
            item.setImg("storage/emulated/0/Download/Screenshot_1526795471.png");
            item.setName("kong" + i);

            contactsItemList.add(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
