package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    Button btn_back;
    Button btn_addok;
    ImageView img_head;

    TextView txt_name;
    TextView txt_phone;
    TextView txt_addr;



    private Uri imageUri;
    private int GET_SYSTEM_PHOTO = 1;
    private int GET_CAMERA = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn_back = (Button) findViewById(R.id.btn_back);
        img_head = (ImageView) findViewById(R.id.img_head);
        btn_addok = (Button) findViewById(R.id.btn_addok);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_phone = (TextView) findViewById(R.id.txt_phone);
        txt_addr = (TextView) findViewById(R.id.txt_addr);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        DatabaseHelper myDatabase = new DatabaseHelper(this, "Contacts.db", null, 3);
        final SQLiteDatabase db =  myDatabase.getWritableDatabase();
        btn_addok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact(db);
                Toast.makeText(AddActivity.this, "该联系人已保存", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 获取图片
         */
        if (requestCode == GET_SYSTEM_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Log.i("Log", "imagePath: " + imagePath);
            showImage(imagePath);
            c.close();
        }
        /**
         * 显示拍好的图片
         */
        if (requestCode == GET_CAMERA && resultCode == Activity.RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                img_head.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 头像添加方式dialog
     */
    private void showDialog() {
        final String[] items = {"系统相册", "拍照", "浏览文件"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(AddActivity.this);
        listDialog.setTitle("选择图片来源");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始
                // ...To-do
                switch (which) {
                    case 0:
                        openPhotoAlbum();
                        break;
                    case 1:
                        openCamera();
                        break;
                    case 2:
                        openSystemFile();
                        break;
                }
            }
        });
        listDialog.show();
    }
    /**
     * 添加联系人
     * @param db
     */
    private void addContact(SQLiteDatabase db){
        /*********************
         * 尚未添加图片！！！  *
         * *******************
         */

        int count = new DatabaseHelper(this, "Contacts.db", null, 3).userCount(txt_name.getText().toString());

        ContentValues values = new ContentValues();
        values.put("name", txt_name.getText().toString());
        values.put("phone", txt_phone.getText().toString());
        values.put("address", txt_addr.getText().toString());
        values.put("count", count);

        Log.i("Log","name" + txt_name.getText().toString());

        db.insert("contacts", null, values);
        values.clear();
    }
    /**
     * 显示系统相册的图片
     * @param imaePath
     */
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        img_head.setImageBitmap(bm);
    }

    /**
     * 打开系统相册
     */
    private void openPhotoAlbum() {
        //动态获取权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AddActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    /**
     * 打开文件
     */
    private void openSystemFile() {
        //动态获取权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (AddActivity.this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Files.getContentUri("/storage"));

                startActivityForResult(intent, 1);
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    /**
     * 打开相机
     */
    private void openCamera(){
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(AddActivity.this, "com.example.myapplication", outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 2);
    }

}
