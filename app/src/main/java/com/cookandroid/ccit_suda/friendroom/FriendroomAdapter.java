package com.cookandroid.ccit_suda.friendroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.R;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkAndUser_list;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.ArrayList;
import java.util.List;

public class FriendroomAdapter extends RecyclerView.Adapter<FriendroomAdapter.ViewHolder> {
    List<TalkAndUser_list> list = new ArrayList<TalkAndUser_list>();
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

    public void setData(List<TalkAndUser_list> list){
        this.list = list;
        notifyDataSetChanged();
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(FriendroomAdapter.ViewHolder holder, int position) {
        String text = list.get(position).talk.getChatlist();
        holder.textView1.setText(text) ;

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return list.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        ViewHolder(View itemView) {
            super(itemView) ;
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.chat_item) ;
        }
    }
}

