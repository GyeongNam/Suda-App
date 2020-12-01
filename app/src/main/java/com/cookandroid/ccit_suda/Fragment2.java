package com.cookandroid.ccit_suda;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.friendroom.FriendroomAdapter;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        context = container.getContext();



        ArrayList<String> list = new ArrayList<>();
        for (int i=0; i<100; i++) {
            list.add(String.format("TEXT %d", i)) ;
        }

        listView = rootView.findViewById(R.id.RecyclerView);



        listView.setLayoutManager(new LinearLayoutManager(context)) ;
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        FriendroomAdapter adapter = new FriendroomAdapter(list) ;
        listView.setAdapter(adapter) ;

        return rootView;
    }
}
