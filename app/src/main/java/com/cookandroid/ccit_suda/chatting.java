package com.cookandroid.ccit_suda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.google.gson.JsonObject;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;

import static android.provider.Settings.System.DATE_FORMAT;


public class chatting extends AppCompatActivity {
    //    String Tag = "chatting";
//    static Socket mSocket;

    ListView listView;
    ChatListAdapter chatListAdapter;
    ArrayList<chat_list> list_itemArrayList;

    private String TAG = "MainActivity";
    private Socket mSocket;
    Button sendBtn;
    LinearLayout contentFrame;
//    private ScrollView scroll;
    EditText replytext;
    boolean err = false;
    private ApiInterface api;
    Toolbar myToolbar;
    String msgcheck;
    String room;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        // Toolbar 생성.
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        sendBtn = findViewById(R.id.sendBtn);
        replytext = findViewById(R.id.replytext);
        listView = findViewById(R.id.inlayout);
        room = getIntent().getExtras().getString("room");
        Log.e("ddd",room);
        list_itemArrayList = new ArrayList<chat_list>();



        chatListAdapter = new ChatListAdapter(chatting.this,list_itemArrayList);



        listView.setAdapter(chatListAdapter);

        EchoOptions options = new EchoOptions();
        options.host = "http://10.100.111.247:6001";
        Echo echo = new Echo(options);
        echo.connect(new EchoCallback() {
            @Override
            public void call(Object... args) {
                Log.d("Success", String.valueOf(args));
            }
        }, new EchoCallback() {
            @Override
            public void call(Object... args) {
                Log.d("Error", String.valueOf(args));
            }
        });

        echo.channel("laravel_database_"+room)
                .listen("chartEvent", new EchoCallback() {
                    @Override
                    public void call(Object... args) {
                        // Event thrown.
                        //JSONObject receivedData = null;
//                        try {
//                            receivedData = new JSONObject(args[0].toString());
                        Log.d("웃기지마랄라", String.valueOf(args[1]));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 msgcheck = replytext.getText().toString();

                check(msgcheck);
                if (!(msgcheck.isEmpty())) {
//                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
//                    contentFrame = findViewById(R.id.inlayout);
//                    LayoutInflater inflater = getLayoutInflater();
//                    LinearLayout linearLayout = (LinearLayout) inflater.inflate( R.layout.sendmsg, null );
//                    inflater.inflate(R.layout.sendmsg,contentFrame,true);

                    SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                    String userinfo = sharedPreferences.getString("userinfo", "");

                    Date now = new Date();
                    chat_list list = new chat_list(userinfo ,now,msgcheck);
                    list_itemArrayList.add(list);
                    chatListAdapter.notifyDataSetChanged();
                    replytext.setText(null); // EditText에 입력받은 값을 전송 후 초기화 시켜주는 부분
                    talkRequest();
//                    scrollDown();
                } else {
                    Toast.makeText(getApplicationContext(), "대화를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

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
//    public void scrollDown(){
//        scroll.post(new Runnable() {
//            @Override
//            public void run() {
//                scroll.fullScroll(ScrollView.FOCUS_DOWN);
//                Log.v("쓰레드","?");
//            }
//        });
//    }

    //        post 방식
    public void talkRequest() {
        String url = "chartEvent"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        String other = "test";
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("sendmsg", msgcheck);
        params.put("user", userinfo);
        params.put("room", room);

        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                TextView userid = (TextView) findViewById(R.id.user_id1);
                TextView sendmsg = (TextView) findViewById(R.id.chat1);
                TextView chattime = (TextView) findViewById(R.id.message_time1);
                userid.setText(userinfo);
                sendmsg.setText(msgcheck);

                Date today = Calendar.getInstance().getTime();
                chattime.setText(today.toString());
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