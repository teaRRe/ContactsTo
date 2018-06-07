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
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private List<ContactsItem> contactsItemList = new ArrayList<ContactsItem>();

    private Button btn_sech;
    private TextView txt_sech;
    private RecyclerView rec_sech;
    private RecyclerView rec_show;
    private View action_setting;

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

        action_setting = findViewById(R.id.action_settings);


        /**
         *  搜索框点击 对应的RecycleView显示和隐藏事件
         */
        btn_sech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txt_sech.getText() != "")
                    txt_sech.setText("");

                // txt_sech: 搜索的输入框

                if (txt_sech.getVisibility() == View.GONE){
                    // 先把搜索框显示出来
                    txt_sech.setVisibility(View.VISIBLE);
                    // 显示搜索 RecycleView
                    rec_sech.setVisibility(View.VISIBLE);
                    // 隐藏主界面 RecycleView
                    rec_show.setVisibility(View.INVISIBLE);
                    setTitle("搜索");
                }
                else {
                    // 隐藏搜索框
                    txt_sech.setVisibility(View.GONE);
                    // 隐藏搜索RecycleView
                    rec_sech.setVisibility(View.GONE);
                    // 显示主界面RecycleView
                    rec_show.setVisibility(View.VISIBLE);
                    setTitle("ContactsTo");
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });


        final DatabaseHelper dp = new DatabaseHelper(this, "Contacts.db", null, 3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rec_sech.setLayoutManager(layoutManager);
        rec_sech.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));

        /**
         *  搜索框输入改变监听事件
         */
        txt_sech.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("Log", "Text Change!" + s);

                List<ContactsItem> list = new ArrayList<>();
                if (!"".equals(s) || s != null)
                    list = dp.onFindByName(MainActivity.this, s.toString());

                SearchAdapter adapter = new SearchAdapter(list);
                rec_sech.setAdapter(adapter);

            }

            @Override
            public void afterTextChanged(Editable s) {}
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
        /**
         * 主界面显示
         */
        //getData();
        updateRec();
    }

    private void readContacts(){
        Cursor cursor = null;
        try {
            // 查询联系人数据
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.
                    Phone.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // 获取联系人姓名
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // 获取联系人手机号
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    ContactsItem phoneContacts = new ContactsItem();

                    phoneContacts.setName(displayName);
                    phoneContacts.setId("p-"+displayName);

                    updateRec();
                    contactsItemList.add(phoneContacts);

                    Log.i("Log","-----------DisplayName: " + displayName + "   number： " + number);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    /**
     * 返回主界面时刷新数据
     */
    protected void onStart(){
        super.onStart();
        updateRec();
    }
    protected void onDestroy(){
        super.onDestroy();
        contactsItemList.clear();
    }


    /**
     * 刷新界面数据
     */
    public void updateRec(){

        contactsItemList =  new DatabaseHelper(this, "Contacts.db", null, 3).getDate(this);

        if (rec_show.getLayoutManager()== null){

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rec_show.setLayoutManager(layoutManager);
            rec_show.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        }

        ContactsAdapter adapter = new ContactsAdapter(contactsItemList);
        rec_show.setAdapter(adapter);
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

        menu.add(Menu.NONE, 10, 2, "读取系统通讯录");

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){

            case R.id.action_settings:
                break;
            case 10:
                getPermission();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void getPermission(){

        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);

        }else {
            readContacts();
        }
    }
}
