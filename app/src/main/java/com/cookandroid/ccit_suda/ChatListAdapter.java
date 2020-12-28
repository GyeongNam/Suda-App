package com.cookandroid.ccit_suda;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private SharedPreferences sharedPreferences;
    Context context;
    List<Talk> list_itemArrayList = new ArrayList<>();
    ViewHolder viewholder;
    TalkDatabase talkDatabase = TalkDatabase.getDatabase(context);
    int room;
    String imgurl = "http://ccit2020.cafe24.com:8082/img/";

    public ChatListAdapter(Context context, int room) {
        this.context = context;
        this.room = room;

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

        //내가 작성한 메시지 일때
        if (sharedPreferences.getString("userinfo", "").equals(list_itemArrayList.get(position).getUser())) {
            if (list_itemArrayList.get(position).getImage_status().equals("1")) {
                //이미지 있을때
                String image_file_name = list_itemArrayList.get(position).getChatlist();
                Picasso.get().load(imgurl + image_file_name).into(holder.right_image);
            } else {
                //일반 메시지 일때
                holder.content_textView.setText(list_itemArrayList.get(position).getChatlist());
            }
            holder.nickname_textView.setText(list_itemArrayList.get(position).getUser());
            holder.date_textView.setText(String.valueOf(list_itemArrayList.get(position).getDate()));
            holder.chat_count_right.setVisibility(View.VISIBLE);
            if (list_itemArrayList.get(position).getCount().equals("0")) {
                holder.chat_count_right.setVisibility(View.GONE);
            } else {
                holder.chat_count_right.setText(String.valueOf(list_itemArrayList.get(position).getCount()));
            }

            Log.e("방인원카운트", String.valueOf(talkDatabase.talkDao().room_user_list_count(room)));

            holder.nickname_textView1.setVisibility(View.GONE);
            holder.content_textView1.setVisibility(View.GONE);
            holder.content_textView3.setVisibility(View.GONE);
            holder.date_textView1.setVisibility(View.GONE);
            holder.chat_count_left3.setVisibility(View.GONE);
            holder.nickname_textView.setVisibility(View.VISIBLE);
            holder.content_textView.setVisibility(View.VISIBLE);
            holder.date_textView.setVisibility(View.VISIBLE);
            holder.chat_count_left.setVisibility(View.GONE);
            holder.parent_right_chat.setVisibility(View.VISIBLE);
            holder.parent_left_chat.setVisibility(View.GONE);

        } else if (list_itemArrayList.get(position).getUser().equals("SYSTEM")) {
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
            holder.parent_right_chat.setVisibility(View.GONE);
            holder.parent_left_chat.setVisibility(View.GONE);

        } else {
            //상대방에게서 받은 메시지일때
            if (list_itemArrayList.get(position).getImage_status().equals("1")) {
                //이미지 있을때
                String image_file_name = list_itemArrayList.get(position).getChatlist();
                Picasso.get().load(imgurl + image_file_name).into(holder.left_image);
            } else {
                holder.content_textView1.setText(list_itemArrayList.get(position).getChatlist());
            }

            holder.nickname_textView1.setText(list_itemArrayList.get(position).getUser());
            holder.date_textView1.setText(String.valueOf(list_itemArrayList.get(position).getDate()));
            holder.chat_count_left.setVisibility(View.VISIBLE);
            if (list_itemArrayList.get(position).getCount().equals("0")) {
                holder.chat_count_left.setVisibility(View.GONE);
            } else {
                holder.chat_count_left.setText(String.valueOf(list_itemArrayList.get(position).getCount()));
            }
            Log.e("방인원카운트", String.valueOf(talkDatabase.talkDao().room_user_list_count(room)));
            holder.parent_left_chat.setVisibility(View.VISIBLE);
            holder.parent_right_chat.setVisibility(View.GONE);
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

    public void getTalkList(List<Talk> list_itemArrayList) {
        this.list_itemArrayList = list_itemArrayList;
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
        TextView chat_count_right, chat_count_left, chat_count_left3;
        TextView content_textView3;
        ImageView left_image, right_image;
        LinearLayout parent_left_chat, parent_right_chat;

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
            left_image = itemView.findViewById(R.id.left_image);
            right_image = itemView.findViewById(R.id.right_image);
            parent_left_chat = itemView.findViewById(R.id.parent_left_chat);
            parent_right_chat = itemView.findViewById(R.id.parent_right_chat);
        }
    }
}
