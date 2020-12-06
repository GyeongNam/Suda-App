package com.cookandroid.ccit_suda.friendroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.R;
import com.cookandroid.ccit_suda.chatting;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkAndRoom_list;

import java.util.ArrayList;
import java.util.List;

public class FriendroomAdapter extends RecyclerView.Adapter<FriendroomAdapter.ViewHolder> {
    List<TalkAndRoom_list> list = new ArrayList<>();
    Context context;


    // 생성자에서 데이터 리스트 객체를 전달받음.
    public FriendroomAdapter(Context context) {
        this.context = context;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public FriendroomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.friendroom, parent, false) ;
        FriendroomAdapter.ViewHolder vh = new FriendroomAdapter.ViewHolder(view) ;

        return vh ;
    }

    public void setData(List<TalkAndRoom_list> list){
        this.list = list;
        notifyDataSetChanged();
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FriendroomAdapter.ViewHolder holder, int position) {
//        String text = list.get(position).getChatlist();
//        String text2 = list.get(position).getUser();
//        String text3 = list.get(position).getDate();
        SharedPreferences sharedPreferences = context.getSharedPreferences("File",0);
        String userinfo = sharedPreferences.getString("userinfo","");
        String text = list.get(position).talk.getChatlist();
        String text2 = list.get(position).room_list.getUser();
        if(!list.get(position).room_list.getRoom_name().equals("null")){
            Log.e("단체 채팅방 이름으로 변경","true");
            text2 = list.get(position).room_list.getRoom_name();
        }
        String text3 = list.get(position).talk.getDate();
        holder.textView1.setText(text);
        holder.textView2.setText(text2);
        holder.textView3.setText(text3);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chatting.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//                Log.e("방버호",String.valueOf(list.get(position).getChat_room()));
//                intent.putExtra("room",String.valueOf(list.get(position).getChat_room()));
                Log.e("방버호",String.valueOf(list.get(position).talk.getChat_room()));
                intent.putExtra("room",String.valueOf(list.get(position).talk.getChat_room()));
                v.getContext().startActivity(intent);
            }
        });
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        TextView textView2 ;
        TextView textView3 ;
        LinearLayout linearLayout;
        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.chat_item) ;
            textView2 = itemView.findViewById(R.id.chat_name) ;
            textView3 = itemView.findViewById(R.id.chat_tiem) ;
            linearLayout = itemView.findViewById(R.id.linearLayout2) ;
        }
    }
}

