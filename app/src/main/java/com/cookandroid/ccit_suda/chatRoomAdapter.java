package com.cookandroid.ccit_suda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cookandroid.ccit_suda.room.Talk;

import java.util.ArrayList;
import java.util.List;

public class chatRoomAdapter extends BaseAdapter {
    Context context;
    ArrayList<chatroom_list> list_chatroomArrayList;

    TextView chatroom_user_name;
    TextView Last_talk;
    TextView Last_talk_time;

    public chatRoomAdapter(Context context, ArrayList<chatroom_list> list_chatroomArrayList) {
        this.context = context;
        this.list_chatroomArrayList = list_chatroomArrayList;
    }

    @Override
    public int getCount() {
        return this.list_chatroomArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_chatroomArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chatroom, null);

            chatroom_user_name = (TextView)convertView.findViewById(R.id.chatroom_user_name);
            Last_talk = (TextView)convertView.findViewById(R.id.Last_talk);
            Last_talk_time = (TextView)convertView.findViewById(R.id.Last_talk_time);

        }
        chatroom_user_name.setText(list_chatroomArrayList.get(position).getChatroom_user_name());
        Last_talk.setText(list_chatroomArrayList.get(position).getLast_talk());
        Last_talk_time.setText(list_chatroomArrayList.get(position).getLast_talk_time());

        return convertView;
    }

    public void getTalkUserList(List<Talk> talks) {
    }
}
