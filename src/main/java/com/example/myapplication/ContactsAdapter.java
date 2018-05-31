package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

/**
 * Created by TEARREAL on 2018/5/20.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<ContactsItem> mContacts;

    public ContactsAdapter(List<ContactsItem> list){
        mContacts = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View contactView;
        ImageView item_img;
        TextView item_name;
        TextView item_COUNT;
        public ViewHolder(View view){
            super(view);
            contactView = view;
            item_img = (ImageView) view.findViewById(R.id.item_img);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_COUNT = (TextView) view.findViewById(R.id.item_COUNT);
            }
        }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ietms, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = holder.getAdapterPosition();
                ContactsItem item = mContacts.get(position);

                Intent intent = new Intent(v.getContext(), DetailesActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("count", item.getCOUNT());
                intent.putExtra("id", item.getId());

                v.getContext().startActivity(intent);

                //Toast.makeText(v.getContext(), "you click " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.contactView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                ContactsItem item = mContacts.get(position);
                showDialog(v.getContext(), item.getId(), item.getName());
                return false;
            }
        });

        return holder;
    }

    /**
     * 绑定每一项的数据
     * 设置默认头像的方法，这里是底层
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsItem item = mContacts.get(position);
        try {
            holder.item_img.setImageURI(Uri.parse(item.getImg()));
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.item_name.setText(item.getName());
        holder.item_COUNT.setText(item.getCOUNT());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * 长按弹出dialog
     * @param context
     */
    private void showDialog(final Context context, final String id, final String name) {

        final String[] items = {"删除", "查看","拨打电话", "发送短信"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        listDialog.setTitle("选择");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        showNormalDialog(context, id, name);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        listDialog.show();
    }

    /**
     *
     * @param context
     * @param id
     * @param name
     */
    private void showNormalDialog(final Context context, String id, final String name){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final Context mcontext = context;
        final String mid = id;
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mcontext);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("确认删除么?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /**
                         * 删除以后更新数据
                         */

                        DatabaseHelper dp = new DatabaseHelper(mcontext, "Contacts.db", null, 3);

                        dp.onDelete(mid);

                        List<ContactsFullInfo> list = dp.selectByName(name);

                        for (int i = 0; i < list.size();i++){
                            dp.onUpdateAfterDelete( list.get(i).getId(), String.valueOf(i+1), name);
                        }

                        Intent intent = new Intent(mcontext, MainActivity.class);
                        mcontext.startActivity(intent);

                        dp.close();

                    }
                });

        normalDialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
        //显示
        normalDialog.show();
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

}
