package com.cookandroid.ccit_suda;

import android.content.Context;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.sendmsg, null);

            viewholder = new ViewHolder();

            viewholder.nickname_textView = (TextView)convertView.findViewById(R.id.user_id1);
            viewholder.content_textView = (TextView)convertView.findViewById(R.id.chat1);
            viewholder.date_textView = (TextView)convertView.findViewById(R.id.message_time1);
            convertView.setTag(viewholder);

        }
        else{
            viewholder = (ViewHolder)convertView.getTag();
        }

        viewholder.nickname_textView.setText(list_itemArrayList.get(position).getNickname());
        viewholder.content_textView.setText(list_itemArrayList.get(position).getContent());
        viewholder.date_textView.setText(list_itemArrayList.get(position).getWrite_date().toString());


        return convertView;
    }


}
