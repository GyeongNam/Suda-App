package com.cookandroid.ccit_suda;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.AsyncPagedListDiffer;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private SharedPreferences sharedPreferences;
    Context context;
    List<Talk> list_itemArraylist = new ArrayList<>();
    //    ViewHolder viewholder;
//private final AsyncPagedListDiffer<Talk> mDiffer;

    ViewHolder0 viewHolder0;
    ViewHolder1 viewHolder1;
    ViewHolder2 viewHolder2;
    Talk talk;
    TalkDatabase talkDatabase = TalkDatabase.getDatabase(context);
    int room;
    String imgurl = HttpClient.imageurl;
//    private static DiffUtil.ItemCallback<Talk> DIFF_CALLBACK =
//            new DiffUtil.ItemCallback<Talk>() {
//                // Concert details may have changed if reloaded from the database,
//                // but ID is fixed.
//                @Override
//                public boolean areItemsTheSame(Talk oldConcert, Talk newConcert) {
//                    return oldConcert.getIdx() == newConcert.getIdx();
//
//                }
//
//                @Override
//                public boolean areContentsTheSame(Talk oldConcert,
//                                                  Talk newConcert) {
//                    Log.e("보자보자","oldConcert : " + oldConcert.getChat_idx() + " newConcert : " + newConcert.getChat_idx());
//                    return oldConcert.getChat_idx().equals(newConcert.getChat_idx());
//                }
//            };

//    protected ChatListAdapter(Context context, int room) {
//        super(DIFF_CALLBACK);
//        mDiffer = new AsyncPagedListDiffer(this, DIFF_CALLBACK);
//        setHasStableIds(true);
//        this.context = context;
//        this.room = room;
//    }

//    public void submitList(PagedList<Talk> pagedList) {
//        mDiffer.submitList(pagedList);
//    }

    public ChatListAdapter(Context context, int room) {
        this.context = context;
        this.room = room;

    }


//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

    @Override
    public int getItemViewType(int position) {
        sharedPreferences = context.getSharedPreferences("File", 0);
//         talk = mDiffer.getItem(position);
        if (list_itemArraylist.get(position).getUser().equals(sharedPreferences.getString("userinfo", ""))) {
            return 0;
        } else if (list_itemArraylist.get(position).getUser().equals("SYSTEM")) {
            return 2;
        } else {
            return 1;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
                return new ViewHolder0(view);
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);
                return new ViewHolder1(view);
        }
        view = LayoutInflater.from(context).inflate(R.layout.chat_exit, parent, false);
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String userinfo = sharedPreferences.getString("userinfo", "");
//        내가보낸 메시지일때
//       talk = mDiffer.getItem(position);
//        Log.e("몇개들어있노노",String.valueOf(getItemCount()));
//        if(talk !=null){
////            Log.e("뭐냐",String.valueOf(talk));
//
//
//        }else {
//            // Null defines a placeholder item - PagedListAdapter will automatically invalidate
//            // this row when the actual object is loaded from the database
//
//        }

        if (sharedPreferences.getString("userinfo", "").equals(list_itemArraylist.get(position).getUser())) {
            viewHolder0 = (ViewHolder0) holder;
            viewHolder0.chat_count_right.setText(String.valueOf(list_itemArraylist.get(position).getCount()));
            if (String.valueOf(list_itemArraylist.get(position).getCount()).equals("0")) {
                viewHolder0.chat_count_right.setVisibility(View.GONE);
            } else {
                viewHolder0.chat_count_right.setVisibility(View.VISIBLE);
            }
            viewHolder0.content_textView.setText(list_itemArraylist.get(position).getChatlist());
            viewHolder0.date_textView.setText(String.valueOf(list_itemArraylist.get(position).getDate()));
            viewHolder0.nickname_textView.setText(list_itemArraylist.get(position).getUser());
            if (list_itemArraylist.get(position).getImage_status().equals("1")) {
                String image_file_name = list_itemArraylist.get(position).getImage_uri();
//                Picasso.get().load(imgurl + image_file_name).into(viewHolder0.right_image);
                Glide.with(context).load(imgurl + image_file_name).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(viewHolder0.right_image);
                viewHolder0.content_textView.setVisibility(View.GONE);
                viewHolder0.right_image.setVisibility(View.VISIBLE);
            } else {
                viewHolder0.right_image.setVisibility(View.GONE);
                viewHolder0.content_textView.setVisibility(View.VISIBLE);
            }
        }

        //나가기 표시했을때
        else if (list_itemArraylist.get(position).getUser().equals("SYSTEM")) {
            viewHolder2 = (ViewHolder2) holder;
            viewHolder2.chat3.setText(list_itemArraylist.get(position).getChatlist());

        }
        //상대에게서 온 메시지일 때
        else {
            viewHolder1 = (ViewHolder1) holder;
            viewHolder1.chat_count_left.setText(String.valueOf(list_itemArraylist.get(position).getCount()));
            if (String.valueOf(list_itemArraylist.get(position).getCount()).equals("0")) {
                viewHolder1.chat_count_left.setVisibility(View.GONE);
            } else {
                viewHolder1.chat_count_left.setVisibility(View.VISIBLE);
            }
            viewHolder1.content_textView1.setText(list_itemArraylist.get(position).getChatlist());
            viewHolder1.date_textView1.setText(String.valueOf(list_itemArraylist.get(position).getDate()));
            viewHolder1.nickname_textView1.setText(list_itemArraylist.get(position).getUser());
            if (list_itemArraylist.get(position).getImage_status().equals("1")) {
                String image_file_name = list_itemArraylist.get(position).getImage_uri();
//                Picasso.get().load(imgurl + image_file_name).into(viewHolder1.left_image);
                Glide.with(context).load(imgurl + image_file_name).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(viewHolder1.left_image);
                viewHolder1.content_textView1.setVisibility(View.GONE);
                viewHolder1.left_image.setVisibility(View.VISIBLE);
            } else {
                viewHolder1.content_textView1.setVisibility(View.VISIBLE);
                viewHolder1.left_image.setVisibility(View.GONE);
            }
        }


    }

    public void getTalkList(List<Talk> list_itemArraylist) {
        this.list_itemArraylist = list_itemArraylist;
        notifyDataSetChanged();
    }
//
    @Override
    public int getItemCount() {
//        return mDiffer.getItemCount();
        return list_itemArraylist.size();
    }


    class ViewHolder0 extends RecyclerView.ViewHolder {
        TextView nickname_textView, content_textView, date_textView, chat_count_right;
        ImageView right_image;
        LinearLayout parent_right_chat;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            nickname_textView = itemView.findViewById(R.id.user_id1);
            content_textView = itemView.findViewById(R.id.chat1);
            date_textView = itemView.findViewById(R.id.message_time1);
            right_image = itemView.findViewById(R.id.right_image);
            chat_count_right = itemView.findViewById(R.id.chat_count_right);
            parent_right_chat = itemView.findViewById(R.id.parent_right_chat);
        }
    }

    class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView nickname_textView1, content_textView1, date_textView1, chat_count_left;
        ImageView left_image;
        LinearLayout parent_left_chat;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            nickname_textView1 = itemView.findViewById(R.id.user_id2);
            content_textView1 = itemView.findViewById(R.id.chat2);
            date_textView1 = itemView.findViewById(R.id.message_time2);
            left_image = itemView.findViewById(R.id.left_image);
            chat_count_left = itemView.findViewById(R.id.chat_count_left);
            parent_left_chat = itemView.findViewById(R.id.parent_left_chat);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        TextView chat3;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            chat3 = itemView.findViewById(R.id.chat3);
        }
    }
}

