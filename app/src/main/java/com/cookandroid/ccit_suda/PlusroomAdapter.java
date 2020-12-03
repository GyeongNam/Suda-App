package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.room.User_list;

import java.util.ArrayList;
import java.util.List;

class PlusroomAdapter extends RecyclerView.Adapter<PlusroomAdapter.ViewHolder> {
    private final OnItemCheckListener onItemClick;
    Context context;

    List<User_list> list = new ArrayList<>();
    public PlusroomAdapter(Context context,@NonNull OnItemCheckListener onItemCheckListener) {
        this.context = context;
        this.onItemClick = onItemCheckListener;

    }
    interface OnItemCheckListener {
        void onItemCheck(String item);
        void onItemUncheck(String item);
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

         User_list currentItem = list.get(position);
        holder.friend_name.setText(list.get(position).getUser_name());
        holder.friend_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.friend_check.setChecked(!holder.friend_check.isChecked());
                if (holder.friend_check.isChecked()) {
                    onItemClick.onItemCheck(currentItem.getUser_name());
                    Log.v("체크",currentItem.getUser_name());
                } else {
                    onItemClick.onItemUncheck(currentItem.getUser_name());
                    Log.v("체크안될때",currentItem.getUser_name());
                }
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
        CheckBox friend_check;
        View itemView;
        ViewHolder(View itemView) {
            super(itemView) ;
            this.itemView = itemView;
            friend_name = itemView.findViewById(R.id.friend_name);
            friend_check = itemView.findViewById(R.id.friend_check);
            friend_check.setClickable(false);
            // 뷰 객체에 대한 참조. (hold strong reference)
        }
        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}
