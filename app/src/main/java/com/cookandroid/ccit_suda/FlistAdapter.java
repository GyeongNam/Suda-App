package com.cookandroid.ccit_suda;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FlistAdapter extends BaseAdapter {
    Context context;
    ArrayList<friend_list> friend_lists_itemArrayList;
    ViewHolder viewholder;


    class ViewHolder {
        TextView talkusername;
    }

    public FlistAdapter(Context context, ArrayList<friend_list> friend_lists_itemArrayList) {
        this.context = context;
        this.friend_lists_itemArrayList = friend_lists_itemArrayList;
    }

    @Override
    public int getCount() {
        return friend_lists_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return friend_lists_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile,null);
            viewholder = new ViewHolder();
            Log.v("d","d");
            viewholder.talkusername = convertView.findViewById(R.id.talkusername);
            convertView.setTag(viewholder);
        }
        else {
            viewholder = (ViewHolder)convertView.getTag();
        }
        viewholder.talkusername.setText(friend_lists_itemArrayList.get(position).getName());

        return convertView;
    }
}
