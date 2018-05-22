package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    private int CONTACTS_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String count = intent.getStringExtra("count");
        count.substring(0);
        Log.i("Log", "name: " + name + "  count: " + count + " count.substring(0): " + count.substring(1));

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_address = (TextView) findViewById(R.id.txt_addr);
        txt_head = (ImageView) findViewById(R.id.img_head);
        btn_detailes_ok = (Button) findViewById(R.id.btn_details_ok);
        btn_detailes_back = (Button) findViewById(R.id.btn_details_back);

        ContactsFullInfo contactsFullInfo = new DatabaseHelper(this, "Contacts.db", null, 3).getContactFullInoByNameAndCount(name, count.substring(1));


        txt_name.setText(contactsFullInfo.getName());
        txt_phone.setText(contactsFullInfo.getPhone());
        txt_address.setText(contactsFullInfo.getAddress());

    }

    private void onUpdate(){
        String name = txt_name.getText().toString();
        String phone = txt_phone.getText().toString();
        String address = txt_address.getText().toString();

        SQLiteDatabase db = new DatabaseHelper(this, "Contacts.db", null, 2).getWritableDatabase();

    }
}
