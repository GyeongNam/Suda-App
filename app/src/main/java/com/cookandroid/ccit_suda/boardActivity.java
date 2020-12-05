package com.cookandroid.ccit_suda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;

import com.baoyz.widget.PullRefreshLayout;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.CallbackWithRetry;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;

public class boardActivity extends DrawerActivity {
    private long backBtnTime = 0;
    private LinearLayout container1, container2, container3, container4, container5;
    PullRefreshLayout swipe_refresh_layout;
    ScrollView mainboard_scroll;
    EchoOptions options = new EchoOptions();
    Echo echo;
    ArrayList array = new ArrayList();
    ArrayList<String> data1 = new ArrayList<>(3);
    ArrayList<String> data2 = new ArrayList<>(3);
    ArrayList<String> data3 = new ArrayList<>(3);
    ArrayList<String> data4 = new ArrayList<>(3);
    ArrayList<String> data5 = new ArrayList<>(3);
    ArrayList<String> key1 = new ArrayList<>(3);
    ArrayList<String> key2 = new ArrayList<>(3);
    ArrayList<String> key3 = new ArrayList<>(3);
    ArrayList<String> key4 = new ArrayList<>(3);
    ArrayList<String> key5 = new ArrayList<>(3);
    ApiInterface api;
    TalkDatabase talkDatabase;
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        EchoOptions options = new EchoOptions();
//        Echo echo;
//
//        options.host = "http://ccit2020.cafe24.com:6061";
//        echo = new Echo(options);
//
//        echo.connect(new EchoCallback() {
//            @Override
//            public void call(Object... args) {
//                Log.d("Success", String.valueOf(args));
//            }
//        }, new EchoCallback() {
//            @Override
//            public void call(Object... args) {
//                Log.d("Error", String.valueOf(args));
//            }
//        });
//
//        String a =sharedPreferences.getString("room","");
//
//
//        a = a.replace("[","");
//        a = a.replace("]","");
//        a = a.replace(" ","");
//        String[] b = a.split(",");
////        Log.e("보드파괴",sharedPreferences.getString("room",""));
        echo.disconnect();
//        for(int i = 0; i<array.size(); i++){
//
//            echo.leave("laravel_database_"+array.get(i));
//            Log.e("보드파괴",array.get(i).toString());
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        sendRequest();
        swipe_refresh_layout= findViewById(R.id.swipe_refresh_layout);
        mainboard_scroll = findViewById(R.id.mainboard_scroll);
        TalkDatabase db = Room.databaseBuilder(this, TalkDatabase.class,"talk-db").allowMainThreadQueries().build();
        talkDatabase = TalkDatabase.getDatabase(this);
        echoconnet();
        mainboard_scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = mainboard_scroll.getScrollY(); //for verticalScrollView
                Log.v("스크롤",String.valueOf(scrollY));
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
        container1 = (LinearLayout) findViewById(R.id.freeparent);
        container2 = (LinearLayout) findViewById(R.id.dailyparent);
        container3 = (LinearLayout) findViewById(R.id.secretparent);
        container4 = (LinearLayout) findViewById(R.id.nomeanparent);
        container5 = (LinearLayout) findViewById(R.id.mypostparent);
        Log.v("TAG", container1.getClass().getName());

    }

    //텍스트뷰 동적생성하기
    public void textview(final String a, android.widget.LinearLayout container, final String key) {
        //TextView 생성
        final TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(20);
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
                a.appendLog(date + "/M/PostdetailActivity/"+key);
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
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("userid", userinfo);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2",String.valueOf(response.body()));
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                TextView username = (TextView) findViewById(R.id.username);
                username.setText("환영합니다 " + userinfo + " 님");
//                        processResponse(response);
                container1.removeAllViews();
                container2.removeAllViews();
                container3.removeAllViews();
                container4.removeAllViews();
                container5.removeAllViews();
                data1.clear();
                data2.clear();
                data3.clear();
                data4.clear();
                data5.clear();
                key1.clear();
                key2.clear();
                key3.clear();
                key4.clear();
                key5.clear();
//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                Log.v("TAG", response);
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    Log.v("TAG", "zz" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.v("TAG", "원하는 json 배열 얻기" + jsonObject.getString("categorie").indexOf("비밀게시판"));
                        if (jsonObject.getString("categorie_num").equals("1")) {
                            data1.add((jsonArray.getJSONObject(i).getString("Title")));
                            key1.add((jsonArray.getJSONObject(i).getString("post_num")));
                        }
                        if (jsonObject.getString("categorie_num").equals("2")) {
                            data2.add(String.valueOf(jsonArray.getJSONObject(i).getString("Title")));
                            key2.add((jsonArray.getJSONObject(i).getString("post_num")));
                        }
                        if (jsonObject.getString("categorie_num").equals("3")) {
                            data3.add(String.valueOf(jsonArray.getJSONObject(i).getString("Title")));
                            key3.add((jsonArray.getJSONObject(i).getString("post_num")));
                        }
                        if (jsonObject.getString("categorie_num").equals("4")) {
                            data4.add(String.valueOf(jsonArray.getJSONObject(i).getString("Title")));
                            key4.add((jsonArray.getJSONObject(i).getString("post_num")));
                        }
                        if (jsonObject.getString("writer").indexOf(userinfo) == 0) {
                            data5.add(String.valueOf(jsonArray.getJSONObject(i).getString("Title")));
                            key5.add((jsonArray.getJSONObject(i).getString("post_num")));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (data1.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        textview(data1.get(i), container1, key1.get(i));
                    }
                } else {
                    for (int i = 0; i < data1.size(); i++) {
                        textview(data1.get(i), container1, key1.get(i));
                    }
                }
                if (data2.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        textview(data2.get(i), container2, key2.get(i));
                    }
                } else {
                    for (int i = 0; i < data2.size(); i++) {
                        textview(data2.get(i), container2, key2.get(i));
                    }
                }
                if (data3.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        textview(data3.get(i), container3, key3.get(i));
                    }
                } else {
                    for (int i = 0; i < data3.size(); i++) {
                        textview(data3.get(i), container1, key3.get(i));
                    }
                }
                if (data4.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        textview(data4.get(i), container4, key4.get(i));
                    }
                } else {
                    for (int i = 0; i < data4.size(); i++) {
                        textview(data4.get(i), container4, key4.get(i));
                    }
                }
                if (data5.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        textview(data5.get(i), container5, key5.get(i));
                    }
                } else {
                    for (int i = 0; i < data5.size(); i++) {
                        textview(data5.get(i), container5, key5.get(i));
                    }
                }
                swipe_refresh_layout.setRefreshing(false);


                Log.v("TAG", "json데이터 배열담기" + data1);
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
                a.appendLog(date+"/"+"E"+"/boardActivity/" +t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                swipe_refresh_layout.setRefreshing(false);
            }
        });
    }

    public void echoconnet(){
        sharedPreferences = getSharedPreferences("File", 0);
        userinfo = sharedPreferences.getString("userinfo", "");

        String url = "echoroom";
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();

        params.put("userinfo", userinfo);
        Log.e("userinfos",userinfo);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.v("retrofit2",String.valueOf(response.body()));
                try {
                    JSONArray jsonArray = new JSONArray(response.body());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String room = jsonObject.getString("chat_room");



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
                        array.add(room);
                        echo.channel("laravel_database_"+room)
                                .listen("chartEvent", new EchoCallback() {
                                    @Override
                                    public void call(Object... args) {
                                        Date now = new Date();
                                        Log.d("웃기지마랄라", String.valueOf(args[1]));
                                        String qwe;
                                        String qwe1;
                                        int qwe2;
                                        try {
                                            JSONObject jsonObject = new JSONObject(args[1].toString());
                                            chat_list list = new chat_list(jsonObject.getString("user") ,now,jsonObject.getString("message"));
                                            qwe = jsonObject.getString("user");
                                            qwe1 = jsonObject.getString("message");
                                            qwe2 = Integer.parseInt(jsonObject.getString("channel"));
                                            Log.v("1",qwe);
                                            Log.v("1",qwe1);
                                            Log.v("1",String.valueOf(qwe2));
                                            Log.v("1",now.toString());
                                            Talk t = new Talk(null,qwe,qwe1,qwe2,String.valueOf(now));
                                            Log.v("1",String.valueOf(t));
                                            talkDatabase.talkDao().insert(t);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("room",array.toString());
                editor.apply();
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
//                txtResult.setText( "onFailure" );
                a.appendLog(date+"/"+"E"+"/login/" +t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
}

