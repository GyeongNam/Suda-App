package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.ArrayList;
import java.util.List;

public class Chatroom_UserList_Adapter extends RecyclerView.Adapter<Chatroom_UserList_Adapter.ViewHolder> {

    private SharedPreferences sharedPreferences;
//    ArrayList<Chatroom_UserList> list_chatroom_UserList;
    List<Room_list> list_chatroom_UserList = new ArrayList<>();
    Context context;
    ViewHolder viewholder;

    public Chatroom_UserList_Adapter(Context context) {
        this.context = context;
        this.list_chatroom_UserList = list_chatroom_UserList;
    }


    @NonNull
    @Override
    public Chatroom_UserList_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.chat_room_friend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sharedPreferences = context.getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        holder.room_f_list.setText(list_chatroom_UserList.get(position).getUser_name());
    }

    public void getfList(List<Room_list> list_chatroom_UserList) {
        this.list_chatroom_UserList =  list_chatroom_UserList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return list_chatroom_UserList.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView room_f_list;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            room_f_list = itemView.findViewById(R.id.room_friend_list);
        }
    }
}
