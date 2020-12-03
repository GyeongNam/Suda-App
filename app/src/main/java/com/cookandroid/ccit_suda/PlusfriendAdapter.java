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

class PlusfriendAdapter extends RecyclerView.Adapter<PlusfriendAdapter.ViewHolder> {
    Context context;
//    ArrayList<plusfriend_list> list_plusfriendList;


    List<User_list> list = new ArrayList<>();


    public PlusfriendAdapter(Context context) {
        this.context = context;


    }


    @NonNull
    @Override
    public PlusfriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friendcard, parent, false);
        PlusfriendAdapter.ViewHolder holder = new PlusfriendAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PlusfriendAdapter.ViewHolder holder, int position) {
        Log.v("데베",String.valueOf(list.size()));
        Log.v("데베",list.get(position).getUser_name());
        holder.plusname.setText(list.get(position).getUser_name());
        holder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chatting.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("room",list.get(position).getRoom());
                v.getContext().startActivity(intent);
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
        TextView plusname;
        Button chatBtn;
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            plusname = itemView.findViewById(R.id.plusname);
            chatBtn = itemView.findViewById(R.id.chatbtn);
        }
    }
}