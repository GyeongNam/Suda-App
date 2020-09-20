package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class boardActivity extends AppCompatActivity {
    private long backBtnTime = 0;
    private ListView listView;
    private DrawerLayout drawerLayout;
    private View drawerView;
    //    String postlist[];?? check
    ArrayAdapter<String> adapter;
    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_board);
        drawerView = (View) findViewById(R.id.drawer);
        listView = (ListView) findViewById(R.id.freeboard);


        ImageButton btn_open = (ImageButton) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button post =  (Button) findViewById(R.id.bt_postupload);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostUploadActivity.class);
                startActivity(intent);
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                drawerLayout.closeDrawers();
                editor.remove("userinfo");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }));
        sendRequest();
        ListView listview = (ListView) findViewById(R.id.freeboard);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);


        listview.setAdapter(adapter);
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    public void sendRequest() {
        String url = "http://10.0.2.2/main"; //"http://ccit2020.cafe24.com:8082/login";
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


                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                data.add(jsonObject.getString("id"));
                                Log.v("TAG", jsonObject.getString("id"));
                                Log.v("TAG", jsonObject.getString("password"));
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.v("TAG", "json데이터 배열담기" + data);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "에러 ->" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        boardlist list = gson.fromJson(response, boardlist.class);
        if (list != null) {

            Toast.makeText(getApplicationContext(), list.id, Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), list.password, Toast.LENGTH_SHORT).show();
        }

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

