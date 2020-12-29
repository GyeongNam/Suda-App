package com.cookandroid.ccit_suda;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import com.baoyz.widget.PullRefreshLayout;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.CallbackWithRetry;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;
import com.google.gson.Gson;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class boardActivity extends DrawerActivity {
    public static Context context_board;
    private long backBtnTime = 0;
    private LinearLayout inflate;
    PullRefreshLayout swipe_refresh_layout;
    ScrollView mainboard_scroll;
    EchoOptions options = new EchoOptions();
    Echo echo;
    ArrayList array = new ArrayList();
    ApiInterface api;
    TalkDatabase talkDatabase;
    private BackPressCloseHandler backPressCloseHandler;
    View child, view;
    TextView board_categorie;
    LinearLayout mypostparent;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        echo.disconnect();
        Log.e("호출", "액티비티 파괴");
    }

    public void disecho() {
        echo.disconnect();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                Log.e("쿼리문", gson.toJson(talkDatabase.talkDao().get_lately_chat_list()));
                send_lately_chat_idx(gson.toJson(talkDatabase.talkDao().get_lately_chat_list()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        context_board = this;
        mypostparent = findViewById(R.id.mypostparent);
        sendRequest();
        backPressCloseHandler = new BackPressCloseHandler(this);
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        mainboard_scroll = findViewById(R.id.mainboard_scroll);
        talkDatabase = TalkDatabase.getDatabase(this);

        String chfcm = getIntent().getStringExtra("room");
        if (chfcm != null) {
            if(echo != null){
                Log.i("echo가", "널이 아니네요");
            }else {
                Log.i("echo가", "널이네여");
            }
            Log.i("과연2", chfcm);
            Intent intent = new Intent(getApplicationContext(), chatting.class);
            intent.putExtra("room", chfcm);
            startActivity(intent);
        }
        else {
            Log.i("과연2", "널이네요");
        }



        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

//                Log.e("쿼리문", gson.toJson(talkDatabase.talkDao().get_lately_chat_list()));
                send_lately_chat_idx(gson.toJson(talkDatabase.talkDao().get_lately_chat_list()));
            }
        });


//        echoconnet();
        mainboard_scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = mainboard_scroll.getScrollY(); //for verticalScrollView
                Log.v("스크롤", String.valueOf(scrollY));
                if (scrollY == 0)
                    swipe_refresh_layout.setEnabled(true);
                else
                    swipe_refresh_layout.setEnabled(false);
            }
        });

        swipe_refresh_layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });


//텍스트뷰 부모 리니어레이아웃
        inflate = findViewById(R.id.inflate);


    }

    //텍스트뷰 동적생성하기
    public void textview(final String a, android.widget.LinearLayout container, final String key) {
        //TextView 생성
        final TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(12);
        view1.setTextColor(Color.BLACK);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 30, 10, 30);


        view1.setLayoutParams(lp);

        view1.setOnClickListener(new View.OnClickListener() {
            log a = new log();

            @Override
            public void onClick(View v) {
                Log.v("TAG", key);
                a.appendLog(date + "/M/PostdetailActivity/" + key);
                Intent intent = new Intent(getApplicationContext(), PostdetailActivity.class);
                intent.putExtra("primarykey", key);
                startActivity(intent);
            }
        });

        //부모 뷰에 추가

        container.addView(view1);
    }

    public void sendRequest() {
        String url = "main"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("userid", userinfo);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2", String.valueOf(response.body()));
                Log.e("지금부터", response.body());
                try {
                    inflate.removeAllViews();
                    mypostparent.removeAllViews();
                    JSONObject response_data = new JSONObject(response.body());
                    JSONArray post_data = new JSONArray(response_data.getString("post_data"));
                    JSONArray data = new JSONArray(response_data.getString("data"));
                    JSONArray mypost = new JSONArray(response_data.getString("mypost"));
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        child = getLayoutInflater().inflate(R.layout.board_inflate, null);
                        child.setId(i);
                        inflate.addView(child);
                        view = inflate.findViewById(i);
                        board_categorie = view.findViewById(R.id.board_categorie);
                        board_categorie.setText(jsonObject.getString("categorie"));
                        for (int a = 0; a < post_data.length(); a++) {
                            JSONObject jsonObject1 = post_data.getJSONObject(a);
                            if (jsonObject.getString("categorie").equals(jsonObject1.getString("categorie"))) {
                                LinearLayout linearLayout = view.findViewById(R.id.board_title);
                                textview(jsonObject1.getString("Title"), linearLayout, jsonObject1.getString("post_num"));
                            }
                        }
                    }
                    for (int i = 0; i < mypost.length(); i++) {
                        JSONObject jsonObject = mypost.getJSONObject(i);
                        textview(jsonObject.getString("Title"), mypostparent, jsonObject.getString("post_num"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                TextView username = (TextView) findViewById(R.id.username);
                username.setText("환영합니다 " + userinfo + " 님");

                swipe_refresh_layout.setRefreshing(false);


            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
                a.appendLog(date + "/" + "E" + "/boardActivity/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }

    public void echoconnet() {
        sharedPreferences = getSharedPreferences("File", 0);
        userinfo = sharedPreferences.getString("userinfo", "");

        String url = "echoroom";
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();

        params.put("userinfo", userinfo);
        Log.e("userinfos", userinfo);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.v("retrofit2", String.valueOf(response.body()));

                //방번호 테이블 insert
                try {
                    JSONObject data = new JSONObject(response.body());
                    JSONArray room_list_array = new JSONArray(data.getString("room_list"));
                    JSONArray user_list_array = new JSONArray(data.getString("user_list"));
                    for (int i = 0; i < room_list_array.length(); i++) {
                        JSONObject room_list = room_list_array.getJSONObject(i);
                        String user = room_list.getString("user");
                        String chat_room = room_list.getString("chat_room");
                        String room_name = room_list.getString("room_name");
                        String lately_chat_idx = room_list.getString("lately_chat_idx");
                        Room_list room_data = new Room_list(null, user, chat_room, room_name, "0");


                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (!talkDatabase.talkDao().isRowIsExist_user_room_list(user, chat_room)) {
                                    talkDatabase.talkDao().insert_room_list(room_data);
                                }
                                if (!talkDatabase.talkDao().isRowIsExist_update_status(user, chat_room, lately_chat_idx)) {
                                    if (userinfo.equals(user)) {

                                    } else {
                                        talkDatabase.talkDao().update_talk_contents_count(user, chat_room);
                                    }

                                    talkDatabase.talkDao().update_lately_chat_idx(user, lately_chat_idx, chat_room);
                                }

                            }
                        });


                    }
                    //insert end

                    //유저리스트 테이블 insert
                    for (int i = 0; i < user_list_array.length(); i++) {
                        JSONObject jsonObject = user_list_array.getJSONObject(i);
                        String follow = jsonObject.getString("follow");
                        String room = jsonObject.getString("room_idx");

                        User_list user_list = new User_list(null, follow, room);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (!talkDatabase.talkDao().isRowIsExist_user_list(follow)) {
                                    talkDatabase.talkDao().insert_user_list(user_list);
                                }
                            }
                        });

                    }
                    //insert end

                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            options.host = "http://ccit2020.cafe24.com:6001";
                            echo = new Echo(options);

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
                            for (int i = 0; i < talkDatabase.talkDao().get_room_number(userinfo).size(); i++) {
                                Log.e("룸 리스트", String.valueOf(talkDatabase.talkDao().get_room_number(userinfo).get(i).getRoom_number()));
                                String room_number = talkDatabase.talkDao().get_room_number(userinfo).get(i).getRoom_number();
                                Log.e("dzdz", room_number);
                                array.add(room_number);

                                echo.channel("laravel_database_" + room_number)
                                        .listen("chartEvent", new EchoCallback() {
                                            @Override
                                            public void call(Object... args) {
                                                Log.d("웃기지마랄라 일반", String.valueOf(args[1]));
                                                String user;
                                                String message;
                                                String chat_idx;
                                                String time;
                                                String user_count;
                                                String image_status;
                                                int channel;
                                                try {
                                                    Intent intent = new Intent("msg" + room_number);

                                                    JSONObject jsonObject = new JSONObject(args[1].toString());
                                                    image_status = jsonObject.getString("image_status");

                                                    user = jsonObject.getString("user");
                                                    message = jsonObject.getString("message");
                                                    channel = Integer.parseInt(jsonObject.getString("channel"));
                                                    chat_idx = jsonObject.getString("chat_idx");
                                                    time = jsonObject.getString("time");
                                                    user_count = jsonObject.getString("user_count");
                                                    Talk t = new Talk(null, user, message, channel, time, chat_idx, "0", user_count, image_status);
                                                    Log.v("1", String.valueOf(t));
                                                    talkDatabase.talkDao().insert(t);
                                                    intent.putExtra("user", user);
                                                    intent.putExtra("message", message);
                                                    intent.putExtra("channel", String.valueOf(channel));
                                                    intent.putExtra("chat_idx", chat_idx);
                                                    intent.putExtra("time", time);
                                                    intent.putExtra("image_status",image_status);
                                                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                            }
//초대되었을때 열어주는 채널
                            Log.e("npe", String.valueOf(userinfo));
                            echo.channel("laravel_database_" + userinfo)
                                    .listen("chartEvent", new EchoCallback() {
                                        @Override
                                        public void call(Object... args) {
                                            Date now = new Date();
                                            Log.d("웃기지마랄라 user", String.valueOf(args[1]));
                                            String user;
                                            String room_name;
                                            String chat_room;
                                            String rooms;
                                            try {
                                                JSONObject jsonObject = new JSONObject(args[1].toString());

                                                if (jsonObject.getString("message").equals("SYSTEM")) {
                                                    user = jsonObject.getString("user");
                                                    rooms = jsonObject.getString("room_name");
                                                    Log.e("한번 보자1", user + " // " + rooms);
                                                    talkDatabase.talkDao().delete_room__lists(rooms, user);
                                                } else if (jsonObject.getString("message").equals("UPDATE")) {
                                                    Log.e("브로드캐스트 json user", jsonObject.getString("user"));
                                                    Log.e("브로드캐스트 json user", jsonObject.getString("chat_idx"));
                                                    Log.e("브로드캐스트 json user", jsonObject.getString("room_name"));
                                                    user = jsonObject.getString("user");
                                                    String lately_chat_idx = jsonObject.getString("chat_idx");
                                                    String room_number = jsonObject.getString("room_name");
                                                    //채팅방 읽음표시 먼저 업데이트
                                                    if (user.equals(userinfo)) {

                                                    } else {
                                                        talkDatabase.talkDao().update_talk_contents_count(user, room_number);
                                                    }
                                                    //후에 각 채팅방 유저마다 어디까지 읽었는지 업데이트
                                                    talkDatabase.talkDao().update_lately_chat_idx(user, lately_chat_idx, room_number);
                                                } else {
                                                    user = jsonObject.getString("user");
                                                    chat_room = jsonObject.getString("message");
                                                    room_name = jsonObject.getString("room_name");
                                                    JSONArray jsonArray = new JSONArray(user);
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        String a = jsonArray.getString(i);
                                                        Log.e("jsonarray", a);
                                                        Log.e("jsonarray", chat_room);
                                                        Log.e("jsonarray", room_name);
                                                        //초대된 방 roo_list 테이블에 데이터 insert
                                                        Room_list room_list = new Room_list(null, a, chat_room, room_name, "0");
                                                        if (!talkDatabase.talkDao().isRowIsExist_user_room_list(a, chat_room)) {
                                                            talkDatabase.talkDao().insert_room_list(room_list);
                                                        }

                                                    }
                                                    Log.e("jsonarray", jsonArray.toString());


//                                                    Echo echo2;
//                                                    echo2 = new Echo(options);
//                                                    echo2.connect(new EchoCallback() {
//                                                        @Override
//                                                        public void call(Object... args) {
//                                                            Log.d("Success", String.valueOf(args));
//                                                        }
//                                                    }, new EchoCallback() {
//                                                        @Override
//                                                        public void call(Object... args) {
//                                                            Log.d("Error", String.valueOf(args));
//                                                        }
//                                                    });
//                                                    echo2.channel("laravel_database_" + chat_room)
//                                                            .listen("chartEvent", new EchoCallback() {
//                                                                @Override
//                                                                public void call(Object... args) {
//                                                                    Log.d("웃기지마랄라 user 안", String.valueOf(args[1]));
//                                                                    String user;
//                                                                    String message;
//                                                                    int channel;
//                                                                    String chat_idx;
//                                                                    String time;
//                                                                    String user_count;
//                                                                    String image_status;
//                                                                    try {
//                                                                        JSONObject jsonObject = new JSONObject(args[1].toString());
//                                                                        user = jsonObject.getString("user");
//                                                                        message = jsonObject.getString("message");
//                                                                        channel = Integer.parseInt(jsonObject.getString("channel"));
//                                                                        chat_idx = jsonObject.getString("chat_idx");
//                                                                        time = jsonObject.getString("time");
//                                                                        user_count = jsonObject.getString("user_count");
//                                                                        image_status = jsonObject.getString("image_status");
//                                                                        Talk t = new Talk(null, user, message, channel, time, chat_idx, "0", user_count,image_status);
//                                                                        Log.v("1", String.valueOf(t));
//                                                                        talkDatabase.talkDao().insert(t);
//                                                                    } catch (JSONException e) {
//                                                                        e.printStackTrace();
//                                                                    }
//                                                                }
//                                                            });
//                                                Talk t = new Talk(null, user, message, channel, String.valueOf(now));
//                                                Log.v("1", String.valueOf(t));
//                                                talkDatabase.talkDao().insert(t);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("room", array.toString());
                editor.apply();
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
//                txtResult.setText( "onFailure" );
                a.appendLog(date + "/" + "E" + "/login/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
            }
        });
    }

    public void send_lately_chat_idx(String data) {
        String url = "get_lately_chat_list"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("key", data);
        params.put("user", userinfo);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

                Log.e("retrofit2", String.valueOf(response.body()));

                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    Log.e("길이", String.valueOf(jsonArray.length()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Log.e("하우ㅢ배열", String.valueOf(i));
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String chatnum = jsonObject.getString("chatnum");
                        String user = jsonObject.getString("user");
                        String message = jsonObject.getString("message");
                        String ch_idx = jsonObject.getString("ch_idx");
                        String created_at = jsonObject.getString("created_at");
                        String user_count = jsonObject.getString("chat_status");
                        String img = jsonObject.getString("img");
                        Talk talk;
                        if(img.equals("null")){
                            //이미지 널 일반채팅
                            img = "0";
                             talk = new Talk(null, user, message, Integer.parseInt(ch_idx), created_at, chatnum, "0", user_count,img);
                        }
                        else{
                            //이미지 있을경우
                             talk = new Talk(null, user, img, Integer.parseInt(ch_idx), created_at, chatnum, "0", user_count,"1");
                        }
                        Log.e("유저 카운트", user_count);


                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (!talkDatabase.talkDao().isRowIsExist_talk_list(chatnum)) {
                                    talkDatabase.talkDao().insert(talk);
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                echoconnet();


            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
            }
        });
    }
}


