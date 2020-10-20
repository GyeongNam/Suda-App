package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class boardActivity extends DrawerActivity {
    private long backBtnTime = 0;

    private LinearLayout container1, container2, container3, container4, container5;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        sendRequest();

//텍스트뷰 부모 리니어레이아웃
        container1 = (LinearLayout) findViewById(R.id.freeparent);
        container2 = (LinearLayout) findViewById(R.id.dailyparent);
        container3 = (LinearLayout) findViewById(R.id.secretparent);
        container4 = (LinearLayout) findViewById(R.id.nomeanparent);
        container5 = (LinearLayout) findViewById(R.id.mypostparent);
        Log.v("TAG", container1.getClass().getName());

    }


    public void sendRequest() {
        String url = "http://ccit2020.cafe24.com:8082/main"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                        String userinfo = sharedPreferences.getString("userinfo", "");
                        TextView username = (TextView) findViewById(R.id.username);
                        username.setText("환영합니다 " + userinfo + " 님");
//                        processResponse(response);


//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
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


                        Log.v("TAG", "json데이터 배열담기" + data1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        a.appendLog(date+"/"+"E"+"/boardActivity/" +error.toString());
                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                params.put("userid", userinfo);

                return params;
            }

//            public Map<String, String> getHeader() throws AuthFailureError{
//                Map<String, String> params = new HashMap<String, String >();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
        };
        request.setShouldCache(false);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
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


//    @Override
//    public void onBackPressed () {
////        super.onBackPressed();
////        long curTime = System.currentTimeMillis();
////        long gapTime = curTime - backBtnTime;
////
////        if(0 <= gapTime && 2000 >= gapTime) {
////            super.onBackPressed();
////        }
////        else {
////            backBtnTime = curTime;
////            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
////        }
//
//    }
}

