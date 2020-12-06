package com.cookandroid.ccit_suda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDao;
import com.cookandroid.ccit_suda.room.TalkDatabase;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;


public class chatting extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatListAdapter chatListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String TAG = "MainActivity";
    Button sendBtn;
    EditText replytext;
    boolean err = false;
    private ApiInterface api;
    Toolbar myToolbar;
    String msgcheck;
    int room;
    SharedPreferences sharedPreferences;
    TalkDatabase talkDatabase;
    User_listViewModel viewModel;
    String userinfo;





    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        sharedPreferences = getSharedPreferences("File", 0);
        userinfo = sharedPreferences.getString("userinfo", "");
        TalkDatabase db = Room.databaseBuilder(this, TalkDatabase.class,"talk-db").allowMainThreadQueries().build();



        // Toolbar 생성.
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        sendBtn = findViewById(R.id.sendBtn);
        replytext = findViewById(R.id.replytext);
        recyclerView = findViewById(R.id.inlayout);
        room = Integer.parseInt(getIntent().getExtras().getString("room"));
        Log.e("ddd", String.valueOf(room));
        chatListAdapter = new ChatListAdapter(chatting.this);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.addOnScrollListener(onScrollListener);


        talkDatabase = TalkDatabase.getDatabase(this);
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
        viewModel.get_Talk_listViewModel(room).observe(this, new Observer<List<Talk>>() {
            @Override
            public void onChanged(List<Talk> talks) {
                chatListAdapter.getTalkList(talks);
                scrollDown();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 msgcheck = replytext.getText().toString();
                check(msgcheck);
                if (!(msgcheck.isEmpty())) {
                    Date now = new Date();
                    replytext.setText(null); // EditText에 입력받은 값을 전송 후 초기화 시켜주는 부분
                    talkRequest();
                } else {
                    Toast.makeText(getApplicationContext(), "대화를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.e("onScroll", "온스크롤리스너 실행?");
            LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
            int totalItemCount = layoutManager.getItemCount();
            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

            if (lastVisible >= totalItemCount - 1) {
                Log.e(TAG, "마지막위치를 잡는가?");
            }
        }
    };

    //추가된 소스, ToolBar에 menu.xml을 인플레이트함
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    //ToolBar에 추가된 항목 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.invite:
                Toast.makeText(getApplicationContext(), "친구추가 버튼임", Toast.LENGTH_LONG).show();
                return true;

            case R.id.Exit_chatroom:
                AlertDialog.Builder builder = new AlertDialog.Builder(chatting.this);
                builder.setTitle("채팅 나가기");       //타이틀 지정.
                builder.setMessage("정말 나가시겠습니까?  채팅기록과 채팅방이 사라집니다...");       //메시지
//                builder.setMessage("채팅기록과 채팅방이 사라집니다...");       //메시지
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {        //확인 버튼을 생성 및 클릭시 동작 구현.
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        //"YES" Button Click
                        Toast.makeText(getApplicationContext(), "채팅방을 나갑니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), chattingList.class);
                        startActivity(intent);

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
                return true;

            default:
                Intent intent = new Intent(getApplicationContext(), chattingList.class);
                startActivity(intent);

            return super.onOptionsItemSelected(item);
        }

    }


    public void check(String msgcheck) {
        if (msgcheck.equals("")) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("ERR");
//            builder.setMessage("대화를 입력하세요!");
//            builder.setCancelable(false);
//            builder.setPositiveButton("확인",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            err = true;
//                        }
//                    });
//            builder.show();
        } else {
            err = false;
        }
    }
    public void scrollDown(){
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(chatListAdapter.getItemCount() - 1);
                Log.v("쓰레드","?");
            }
        }, 500);
    }

    //        post 방식
    public void talkRequest() {
        String url = "chartEvent"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        String other = "test";

        params.put("sendmsg", msgcheck);
        params.put("user", userinfo);
        params.put("room", String.valueOf(room));

        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
            //서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2",String.valueOf(response));
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