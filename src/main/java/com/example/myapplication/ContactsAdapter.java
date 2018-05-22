package com.example.myapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
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
import java.util.zip.Inflater;

/**
 * Created by TEARREAL on 2018/5/20.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<ContactsItem> mContacts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View contactView;
        ImageView item_img;
        TextView item_name;
        TextView item_CID;
        public ViewHolder(View view){
            super(view);
            contactView = view;
            item_img = (ImageView) view.findViewById(R.id.item_img);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_CID = (TextView) view.findViewById(R.id.item_CID);
            }
        }
        public ContactsAdapter(List<ContactsItem> list){
            mContacts = list;
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
                intent.putExtra("count", item.getCID());
                v.getContext().startActivity(intent);

                //Toast.makeText(v.getContext(), "you click " + item.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.contactView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                ContactsItem item = mContacts.get(position);
                showDialog(v.getContext());
                return false;
            }
        });
        holder.contactView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_POINTER_DOWN:
                        v.setBackgroundColor(0xffe8e8e8);
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        v.setBackgroundColor(0x00ffffff);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        return holder;
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsItem item = mContacts.get(position);

        holder.item_img.setImageURI(Uri.parse(item.getImg()));
        holder.item_name.setText(item.getName());
        holder.item_CID.setText(item.getCID());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * 长按弹出dialog
     * @param context
     */
    private void showDialog(Context context) {
        final String[] items = {"系统相册", "拍照", "浏览文件"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        listDialog.setTitle("选择");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }
        });
        listDialog.show();
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
