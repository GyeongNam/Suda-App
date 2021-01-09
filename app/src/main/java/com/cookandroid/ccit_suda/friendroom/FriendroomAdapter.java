package com.cookandroid.ccit_suda.friendroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.R;
import com.cookandroid.ccit_suda.chatting;
import com.cookandroid.ccit_suda.databinding.FriendroomBinding;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkAndRoom_list;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FriendroomAdapter extends RecyclerView.Adapter<FriendroomAdapter.ViewHolder> {
    List<TalkAndRoom_list> list = new ArrayList<>();
    Context context;
    Activity activity;

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public FriendroomAdapter(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FriendroomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        FriendroomBinding binding= DataBindingUtil.inflate(inflater,R.layout.friendroom,parent,false);
//        View view = inflater.inflate(R.layout.friendroom, parent, false) ;
        FriendroomAdapter.ViewHolder vh = new FriendroomAdapter.ViewHolder(binding) ;

        return vh ;
    }

    public void setData(List<TalkAndRoom_list> list){
        this.list = list;
        notifyDataSetChanged();
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FriendroomAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("File",0);
        String userinfo = sharedPreferences.getString("userinfo","");
        String text = list.get(position).talk.getChatlist();
        String text2 = list.get(position).room_list.getUser();
        if(!list.get(position).room_list.getRoom_name().equals("null")){
            Log.e("단체 채팅방 이름으로 변경","true");
            text2 = list.get(position).room_list.getRoom_name();
        }
        String text3 = list.get(position).talk.getDate();

        TalkDatabase db;
        db = TalkDatabase.getDatabase(context);


        holder.friendroomBinding.chatItem.setText(text);
        holder.friendroomBinding.chatName.setText(text2);
        holder.friendroomBinding.chatTiem.setText(text3);

        holder.friendroomBinding.linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chatting.class);
                Log.e("방버호",String.valueOf(list.get(position).talk.getChat_room()));
                intent.putExtra("room",String.valueOf(list.get(position).talk.getChat_room()));
                v.getContext().startActivity(intent);
            }
        });
//        holder.friendroomBinding.notReadCount.setText(list.get(position).talk.getCount());
//        if(list.get(position).talk.getCount().equals("0")){
//            holder.friendroomBinding.notReadCount.setVisibility(View.GONE);
//        }else {
//            holder.friendroomBinding.notReadCount.setVisibility(View.VISIBLE);
//        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String not_read_message = db.talkDao().not_read_chat_count(String.valueOf(list.get(position).talk.getChat_room()));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.friendroomBinding.notReadCount.setText(not_read_message);
                        if(not_read_message.equals("0")){
                            holder.friendroomBinding.notReadCount.setVisibility(View.GONE);
                        }else {
                            holder.friendroomBinding.notReadCount.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FriendroomBinding friendroomBinding;

        LinearLayout linearLayout;
        ViewHolder(FriendroomBinding friendroomBinding) {
            super(friendroomBinding.getRoot()) ;
            this.friendroomBinding = friendroomBinding;

        }
    }
}

