package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    Context context;
    ArrayList<chat_list> list_itemArrayList;
    ViewHolder viewholder;

    class ViewHolder {
        TextView nickname_textView;
        TextView content_textView;
        TextView date_textView;
        TextView nickname_textView1;
        TextView content_textView1;
        TextView date_textView1;
    }

    public ChatListAdapter(Context context, ArrayList<chat_list> list_itemArrayList) {
        this.context = context;
        this.list_itemArrayList = list_itemArrayList;
    }

    @Override
    public int getCount() {
        return list_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return list_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("File", 0);
        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.sendmsg, null);
            viewholder = new ViewHolder();
            int type = getItemViewType(position);


            viewholder.nickname_textView = (TextView) convertView.findViewById(R.id.user_id1);
            viewholder.content_textView = (TextView) convertView.findViewById(R.id.chat1);
            viewholder.date_textView = (TextView) convertView.findViewById(R.id.message_time1);

            viewholder.nickname_textView1 = (TextView) convertView.findViewById(R.id.user_id2);
            viewholder.content_textView1 = (TextView) convertView.findViewById(R.id.chat2);
            viewholder.date_textView1 = (TextView) convertView.findViewById(R.id.message_time2);


            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        if(sharedPreferences.getString("userinfo","").equals(list_itemArrayList.get(position).getNickname())){
            viewholder.nickname_textView.setText(list_itemArrayList.get(position).getNickname());
            viewholder.content_textView.setText(list_itemArrayList.get(position).getContent());
            viewholder.date_textView.setText(list_itemArrayList.get(position).getWrite_date().toString());
            viewholder.nickname_textView1.setVisibility(View.GONE);
            viewholder.content_textView1.setVisibility(View.GONE);
            viewholder.date_textView1.setVisibility(View.GONE);
            viewholder.nickname_textView.setVisibility(View.VISIBLE);
            viewholder.content_textView.setVisibility(View.VISIBLE);
            viewholder.date_textView.setVisibility(View.VISIBLE);
        }
        else{
            viewholder.nickname_textView1.setText(list_itemArrayList.get(position).getNickname());
            viewholder.content_textView1.setText(list_itemArrayList.get(position).getContent());
            viewholder.date_textView1.setText(list_itemArrayList.get(position).getWrite_date().toString());
            viewholder.nickname_textView.setVisibility(View.GONE);
            viewholder.content_textView.setVisibility(View.GONE);
            viewholder.date_textView.setVisibility(View.GONE);
            viewholder.nickname_textView1.setVisibility(View.VISIBLE);
            viewholder.content_textView1.setVisibility(View.VISIBLE);
            viewholder.date_textView1.setVisibility(View.VISIBLE);
        }


        return convertView;
    }


}
