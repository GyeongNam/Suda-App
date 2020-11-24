package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class plusfriendAdapter extends BaseAdapter {
    Context context;
    ArrayList<plusfriend_list> list_plusfriendList;
    FriendViewHolder friendViewHolder;



    public plusfriendAdapter(Context context, ArrayList<plusfriend_list> list_plusfriendList) {
        this.context = context;
        this.list_plusfriendList = list_plusfriendList;


    }

    @Override
    public int getCount() {
        return this.list_plusfriendList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_plusfriendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friendcard, null);
            friendViewHolder = new FriendViewHolder();
            friendViewHolder.plusname = (TextView) convertView.findViewById(R.id.plusname);
            friendViewHolder.chatBtn = (Button) convertView.findViewById(R.id.chatbtn);
            //아래 내용 없어서 팅겼었음
            convertView.setTag(friendViewHolder);
            //

        } else {
            friendViewHolder = (FriendViewHolder) convertView.getTag();
        }

        friendViewHolder.plusname.setText(list_plusfriendList.get(position).getName());
        friendViewHolder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chatting.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}
class FriendViewHolder {

    TextView plusname;
    Button chatBtn;

}