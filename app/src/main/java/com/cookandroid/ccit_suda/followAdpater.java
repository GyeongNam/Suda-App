package com.cookandroid.ccit_suda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class followAdpater extends BaseAdapter {
    Context context;
    ArrayList<follow_list> list_followArrayList;
    TextView nameplace_textView;

    public followAdpater(Context context, ArrayList<follow_list> list_followArrayList) {
        this.context = context;
        this.list_followArrayList = list_followArrayList;
    }

    @Override
    public int getCount() { return this.list_followArrayList.size(); }

    @Override
    public Object getItem(int position) { return this.list_followArrayList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.follow_card, null);
            nameplace_textView = (TextView) convertView.findViewById(R.id.nameplace);
        }
        return convertView;
    }
}

