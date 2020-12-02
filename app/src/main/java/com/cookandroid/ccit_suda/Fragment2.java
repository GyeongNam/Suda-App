package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.friendroom.FriendroomAdapter;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.TalkDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Fragment2 extends Fragment {

    private Context context;
    private ApiInterface api;
    RecyclerView listView;
    chatRoomAdapter chatroomadapter;
    ArrayList<chatroom_list> list_chatroomArrayList;
    TalkDatabase talkDatabse;
    User_listViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        context = container.getContext();
        listView = rootView.findViewById(R.id.RecyclerView);
        SharedPreferences sharedPreferences = context.getSharedPreferences("File",0);
        String userinfo = sharedPreferences.getString("userinfo","");
        FriendroomAdapter adapter = new FriendroomAdapter(context) ;

        talkDatabse = TalkDatabase.getDatabase(context);
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);

        viewModel.get_Romm_listViewModel(userinfo).observe(getViewLifecycleOwner(), user ->{
            adapter.setData(user);
        });
        listView.setHasFixedSize(true);


        listView.setLayoutManager(new LinearLayoutManager(context)) ;
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.

        listView.setAdapter(adapter) ;

        return rootView;
    }

}
