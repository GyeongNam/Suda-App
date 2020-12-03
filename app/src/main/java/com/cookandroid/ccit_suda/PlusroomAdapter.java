package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.room.User_list;

import java.util.ArrayList;
import java.util.List;

class PlusroomAdapter extends RecyclerView.Adapter<PlusroomAdapter.ViewHolder> {
    Context context;
    List<User_list> list = new ArrayList<>();
    public PlusroomAdapter(Context context) {
        this.context = context;


    }


    @NonNull
    @Override
    public PlusroomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pls_friend, parent, false);
        PlusroomAdapter.ViewHolder holder = new PlusroomAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlusroomAdapter.ViewHolder holder, int position) {
        Log.v("데베",String.valueOf(list.size()));
        Log.v("데베",list.get(position).getUser_name());
        holder.friend_name.setText(list.get(position).getUser_name());
        holder.friend_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), chatting.class);
////                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("room",list.get(position).getRoom());
//                v.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<User_list> list){
        this.list = list;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView friend_name;
        Button friend_check;
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            friend_name = itemView.findViewById(R.id.friend_name);
            friend_check = itemView.findViewById(R.id.friend_check);
        }
    }
}
