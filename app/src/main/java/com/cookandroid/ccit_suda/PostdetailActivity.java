package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.MultiPartRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostdetailActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private LinearLayout container;
    private ListView postlist;
     List<String> replylist = new ArrayList<>() ;
     private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_postdetail);
        drawerView = (View) findViewById(R.id.drawer);
        container = (LinearLayout) findViewById(R.id.parentlayout);
        postlist = (ListView) findViewById(R.id.postlist);
//        ArrayList replylist = new ArrayList<String>();
        postlist.setVerticalScrollBarEnabled(false);
//        adapter.notifyDataSetChanged();
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

        InputMethodManager controlManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);




        sendRequest();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, replylist);
        postlist.setAdapter(adapter);

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
        String url = "http://10.0.2.2/post_detail"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest (
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                        String userinfo = sharedPreferences.getString("userinfo", "");
                        TextView username = (TextView) findViewById(R.id.username);
                        TextView title = (TextView) findViewById(R.id.Title);
                        TextView text = (TextView) findViewById(R.id.Text);
                        HashMap<Integer,String> map = new HashMap<>();
                        username.setText("환영합니다 " + userinfo + " 님");
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.v("TAG","게시글 디테일"+jsonObject.getString("Title"));
//                                reply.setText(jsonObject.getString("c_writer")+":"+jsonObject.getString("comment"));
                                if(!replylist.contains(jsonObject.getString("c_writer")+":"+jsonObject.getString("comment"))) {

                                    replylist.add(jsonObject.getString("c_writer")+":"+jsonObject.getString("comment"));

                                }
//                                replylist.add(jsonObject.getString("c_writer")+":"+jsonObject.getString("comment"));
                                title.setText(jsonObject.getString("Title"));
                                text.setText(jsonObject.getString("Text"));
                            }
                            Log.v("TAG", String.valueOf(replylist));
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    // 갱신된 데이터 내역을 어댑터에 알려줌
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
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
//                String userinfo = sharedPreferences.getString("userinfo", "");
                Intent intent = getIntent();
                String KEY = intent.getExtras().getString("primarykey");
                params.put("post_num", KEY);
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


























    public void sendreply() {
        String url = "http://10.0.2.2/post_reply"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                        String userinfo = sharedPreferences.getString("userinfo", "");
                        TextView username = (TextView) findViewById(R.id.username);
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);
                        sendRequest();
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
                Intent intent = getIntent();
                EditText replytext = (EditText)findViewById(R.id.replytext);
                String reply = replytext.getText().toString();
                String KEY = intent.getExtras().getString("primarykey");
                params.put("post_num", KEY);
                params.put("reply",reply);
                params.put("writer",userinfo);
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
    public void sendreply(View view){
        sendreply();
    }
    //텍스트뷰 동적생성하기
    public void textview(final String a, android.widget.LinearLayout container){
        //TextView 생성
        final TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(20);
        view1.setTextColor(Color.BLACK);
        Drawable drawable = getResources().getDrawable(R.drawable.round_border);
        view1.setBackground(drawable);
        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30,30,10,30);


        view1.setLayoutParams(lp);



        //부모 뷰에 추가
        container.addView(view1);
    }
}