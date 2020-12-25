package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class ChatDrawer extends AppCompatActivity {
    ApiInterface api;
    RecyclerView listrectclerView;
    Chatroom_UserList_Adapter chatroom_userList_adapter;
    RecyclerView.LayoutManager cLayoutManager;
    String userinfo;
    TalkDatabase talkDatabase;
    User_listViewModel viewModel;
    private String room1;
    Button chat_close;
    Toolbar myToolbar;
    User_listViewModel viewMode;
    private DrawerLayout drawerLayout;
    private View chatdrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_toolbar);
        talkDatabase = TalkDatabase.getDatabase(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.chat_toolbar, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        room1 = getIntent().getExtras().getString("room");


        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        chatroom_userList_adapter = new Chatroom_UserList_Adapter(this);
        userinfo = String.valueOf(getIntent().getExtras().getString("userinfo"));
        Log.e("ddd", String.valueOf(userinfo));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        chatdrawer = (View) findViewById(R.id.chatDrawerView);
        drawerLayout.setDrawerListener(listener);
        // Toolbar 생성.
        ImageButton btn_open = (ImageButton) findViewById(R.id.btn_open);
        chatdrawer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(chatdrawer);
                Toast.makeText(getApplicationContext(), "버튼으로 염", Toast.LENGTH_LONG).show();
            }
        });
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

    @Nullable
    @Override
    public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return super.getDrawerToggleDelegate();
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
                            disconnect();

                            boardActivity a = ((boardActivity)boardActivity.context_board);
                            a.disecho();

                            Toast.makeText(getApplicationContext(), "나갔어요", Toast.LENGTH_LONG).show();
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
    public void disconnect(){
        String url = "disconnect_room"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        userinfo = sharedPreferences.getString("userinfo", "");
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();

        params.put("user1", userinfo);
        params.put("room", room1);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

                String flistArray = response.body();
                Log.v("3",String.valueOf(response.body()));

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        talkDatabase.talkDao().delete_room__list(room1);
                    }
                });
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
}

