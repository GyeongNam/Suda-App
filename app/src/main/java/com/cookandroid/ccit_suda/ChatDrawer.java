 package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
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

    RecyclerView listrectclerView;
    Chatroom_UserList_Adapter chatroom_userList_adapter;
    RecyclerView.LayoutManager cLayoutManager;
    String userinfo;
    TalkDatabase talkDatabase;
    User_listViewModel viewModel;
    private String room1;
    Button chat_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_drawer);
        TalkDatabase db = Room.databaseBuilder(this, TalkDatabase.class,"talk-db").allowMainThreadQueries().build();
        room1 = getIntent().getExtras().getString("room");

        chatroom_userList_adapter = new Chatroom_UserList_Adapter(this);
        userinfo = String.valueOf(getIntent().getExtras().getString("userinfo"));
        Log.e("ddd", String.valueOf(userinfo));

        listrectclerView = findViewById(R.id.chat_p_list);
        listrectclerView.setLayoutManager(cLayoutManager);
        listrectclerView.setAdapter(chatroom_userList_adapter);
        cLayoutManager = new LinearLayoutManager(ChatDrawer.this);

        talkDatabase = TalkDatabase.getDatabase(this);
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
        viewModel.get_Room_friend_list(room1).observe(this, new Observer<List<Room_list>>() {
            @Override
            public void onChanged(List<Room_list> lists) {
                chatroom_userList_adapter.getfList(lists);
                Log.e("채팅방 인원 목록",String.valueOf(lists));
            }
        });

    }
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {

//        drawerLayout.openDrawer(drawerView);

        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {
            Toast.makeText(getApplicationContext(), "드로워 열 림", Toast.LENGTH_LONG).show();

            chat_close = findViewById(R.id.chat_close);
            chat_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatDrawer.this);
                    builder.setTitle("채팅 나가기");       //타이틀 지정.
                    builder.setMessage("정말 나가시겠습니까?  채팅기록과 채팅방이 사라집니다...");       //메시지
                    builder.setMessage("채팅기록과 채팅방이 사라집니다...");       //메시지
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        //확인 버튼을 생성 및 클릭시 동작 구현.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //"YES" Button Click

                            Toast.makeText(getApplicationContext(), "채팅방을 나갑니다.", Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });

                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {       //취소 버튼을 생성하고 클릭시 동작을 구현합니다.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //"NO" Button Click
                            Toast.makeText(getApplicationContext(), "NO Button Click", Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alert = builder.create();                                                       //빌더를 이용하여 AlertDialog객체를 생성합니다.
                    alert.show();
                }
            });
        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    };
}

