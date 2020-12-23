package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private SharedPreferences sharedPreferences;
    Context context;
    List<Talk> list_itemArrayList = new ArrayList<>();
    ViewHolder viewholder;

    public ChatListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.sendmsg, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        if(sharedPreferences.getString("userinfo","").equals(list_itemArrayList.get(position).getUser())){
            holder.content_textView.setText(list_itemArrayList.get(position).getChatlist());
            holder.nickname_textView.setText(list_itemArrayList.get(position).getUser());
            holder.date_textView.setText(String.valueOf(list_itemArrayList.get(position).getDate()));
            holder.nickname_textView1.setVisibility(View.GONE);
            holder.content_textView1.setVisibility(View.GONE);
            holder.content_textView3.setVisibility(View.GONE);
            holder.date_textView1.setVisibility(View.GONE);
            holder.chat_count_left3.setVisibility(View.GONE);
            holder.nickname_textView.setVisibility(View.VISIBLE);
            holder.content_textView.setVisibility(View.VISIBLE);
            holder.date_textView.setVisibility(View.VISIBLE);
            holder.chat_count_left.setVisibility(View.GONE);
        }
        else if(list_itemArrayList.get(position).getUser().equals("SYSTEM")){
            holder.content_textView3.setText(list_itemArrayList.get(position).getChatlist());
            holder.nickname_textView1.setText(list_itemArrayList.get(position).getUser());
            holder.date_textView1.setText(String.valueOf(list_itemArrayList.get(position).getDate()));
            holder.nickname_textView1.setVisibility(View.VISIBLE);
            holder.content_textView3.setVisibility(View.VISIBLE);
            holder.content_textView1.setVisibility(View.GONE);
            holder.date_textView1.setVisibility(View.VISIBLE);
            holder.nickname_textView.setVisibility(View.GONE);
            holder.content_textView.setVisibility(View.GONE);
            holder.date_textView.setVisibility(View.GONE);
            holder.chat_count_right.setVisibility(View.GONE);
            holder.chat_count_left.setVisibility(View.GONE);
            holder.chat_count_left3.setVisibility(View.VISIBLE);
        }
        else{
            holder.content_textView1.setText(list_itemArrayList.get(position).getChatlist());
            holder.nickname_textView1.setText(list_itemArrayList.get(position).getUser());
            holder.date_textView1.setText(String.valueOf(list_itemArrayList.get(position).getDate()));
            holder.nickname_textView1.setVisibility(View.VISIBLE);
            holder.content_textView1.setVisibility(View.VISIBLE);
            holder.date_textView1.setVisibility(View.VISIBLE);
            holder.nickname_textView.setVisibility(View.GONE);
            holder.content_textView.setVisibility(View.GONE);
            holder.date_textView.setVisibility(View.GONE);
            holder.chat_count_right.setVisibility(View.GONE);
            holder.chat_count_left3.setVisibility(View.GONE);
            holder.content_textView3.setVisibility(View.GONE);
        }

    }
    public void getTalkList(List<Talk> list_itemArrayList){
        this.list_itemArrayList =  list_itemArrayList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list_itemArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nickname_textView;
        TextView content_textView;
        TextView date_textView;
        TextView nickname_textView1;
        TextView content_textView1;
        TextView date_textView1;
        TextView chat_count_right,chat_count_left, chat_count_left3;
        TextView content_textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nickname_textView = itemView.findViewById(R.id.user_id1);
            content_textView = itemView.findViewById(R.id.chat1);
            date_textView = itemView.findViewById(R.id.message_time1);
            nickname_textView1 = itemView.findViewById(R.id.user_id2);
            content_textView1 = itemView.findViewById(R.id.chat2);
            date_textView1 = itemView.findViewById(R.id.message_time2);
            chat_count_right = itemView.findViewById(R.id.chat_count_right);
            chat_count_left = itemView.findViewById(R.id.chat_count_left);
            content_textView3 = itemView.findViewById(R.id.chat3);
            chat_count_left3 = itemView.findViewById(R.id.chat_count_left3);
        }
    }
}
