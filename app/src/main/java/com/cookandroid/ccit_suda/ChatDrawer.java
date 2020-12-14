 package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatDrawer extends AppCompatActivity {

    ApiInterface api;
    SharedPreferences sharedPreferences;
    Chatroom_UserList_Adapter chatroom_userList_adapter;
    private RecyclerView.LayoutManager cLayoutManager;
    RecyclerView recyclerView;
    String userinfo;
    TalkDatabase talkDatabase;
    User_listViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_drawer);
        TalkDatabase db = Room.databaseBuilder(this, TalkDatabase.class,"talk-db").allowMainThreadQueries().build();


        recyclerView = findViewById(R.id.chat_p_list);
        recyclerView.setLayoutManager(cLayoutManager);
        chatroom_userList_adapter = new Chatroom_UserList_Adapter(ChatDrawer.this);
        cLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setAdapter(chatroom_userList_adapter);


        userinfo = String.valueOf(getIntent().getExtras().getString("userinfo"));
        Log.e("ddd", String.valueOf(userinfo));

        talkDatabase = TalkDatabase.getDatabase(this);
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
        viewModel.get_Room_friend_list(userinfo).observe(this, new Observer<List<Room_list>>() {
            @Override
            public void onChanged(List<Room_list> lists) {
                chatroom_userList_adapter.getfList(lists);
            }
        });

    }


    //        post 방식
    public void chatpeople() {
        String url = "chatpeoplelist"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("key", userinfo);
//        params.put();
//        params.put("room", String.valueOf(room));
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2",String.valueOf(response.body()));
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
}

