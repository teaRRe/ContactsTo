package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by TEARREAL on 2018/5/21.
 */

public class DetailesActivity extends AppCompatActivity {

    private TextView txt_name;
    private TextView txt_phone;
    private TextView txt_address;
    private ImageView txt_head;
    private Button btn_detailes_ok;
    private Button btn_detailes_back;

    private ImageView btn_call;
    private ImageView btn_msg;

    private int CONTACTS_ID;

    private String ORIGIN_NAME;
    private String _ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String count = intent.getStringExtra("count");
        String id = intent.getStringExtra("id");



        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_address = (TextView) findViewById(R.id.txt_addr);
        txt_head = (ImageView) findViewById(R.id.img_head);
        btn_detailes_ok = (Button) findViewById(R.id.btn_details_ok);
        btn_detailes_back = (Button) findViewById(R.id.btn_details_back);
        btn_call = (ImageView) findViewById(R.id.btn_call);
        btn_msg = (ImageView) findViewById(R.id.btn_msg);

        //根据唯一ID来查询

        if (!id.contains("p-")){
            ContactsFullInfo contactsFullInfo = new  DatabaseHelper(this, "Contacts.db", null, 3).getFullInfoById(id);
            ORIGIN_NAME = contactsFullInfo.getName();

            /**
             * 显示在详情界面上
             * 头像尚未解析
             */
            txt_name.setText(contactsFullInfo.getName());
            txt_phone.setText(contactsFullInfo.getPhone());
            txt_address.setText(contactsFullInfo.getAddress());
        }

        _ID = id;

        btn_detailes_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateContacts(_ID, txt_name.getText().toString(), txt_phone.getText().toString(),
                                txt_address.getText().toString(), "" );

                Toast.makeText(DetailesActivity.this, "联系人更新成功", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(DetailesActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        btn_detailes_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (null != txt_phone.getText() || !"".equals(txt_phone.getText())) {
                        if (DetailesActivity.this.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                                == PackageManager.PERMISSION_GRANTED) {
                            Uri uri = Uri.parse("tel:" + txt_phone.getText());
                            Intent intent = new Intent(Intent.ACTION_CALL, uri);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DetailesActivity.this, "No Permission!", Toast.LENGTH_SHORT).show();
                            requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                        }
                    }
                }
            }
        });

        btn_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    if (null != txt_phone.getText() || !"".equals(txt_phone.getText())) {

                        if (DetailesActivity.this.checkSelfPermission(Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_GRANTED) {
                            Uri uri = Uri.parse("smsto:" + txt_phone.getText());
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DetailesActivity.this, "No Permission!", Toast.LENGTH_SHORT).show();
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                        }

                    }
                }
            }
        });

    }

    /**
     * 更新数据
     * @param _id
     * @param _name
     * @param _phone
     * @param _address
     * @param _head
     */
    private void UpdateContacts(String _id, String _name, String _phone, String _address, String _head){

        ContactsFullInfo contactsFullInfo = new ContactsFullInfo(_name, _phone, _head, _address);
        // 不改名字，COUNT不变
        if (ORIGIN_NAME.equals(txt_name.getText().toString())){

            new DatabaseHelper(this, "Contacts.db", null, 3).onUpdate(_ID, contactsFullInfo);

        }else{
            // 改了名字？？？删了原来的，重新加吧。
            // 此时ID会变
            DatabaseHelper dp = new DatabaseHelper(this, "Contacts.db", null, 3);
            dp.onDelete(_ID);
            dp.onAdd(contactsFullInfo);
        }
        /**
         *
         */
        DatabaseHelper dp = new DatabaseHelper(DetailesActivity.this, "Contacts.db", null, 3);

        List<ContactsFullInfo> list = dp.selectByName(_name);

        for (int i = 0; i < list.size();i++){
            dp.onUpdateAfterDelete( list.get(i).getId(), String.valueOf(i+1), _name);
        }
    }
}
