package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostListActivity extends DrawerActivity {
    TextView maintitle;
    Intent intent;
    String primarykey;
    String mypost;
    LinearLayout freeparent;
    ArrayList<String> List = new ArrayList<>(3);
    ArrayList<String> ListKey = new ArrayList<>(3);
    log a = new log();
    SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    Date date1 = new Date();
    String date = format1.format(date1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        freeparent = findViewById(R.id.freeparent);
        maintitle = (TextView)findViewById(R.id.maintitle);
        Intent intent = getIntent();
        mypost = intent.getExtras().getString("mypost");
        primarykey = intent.getExtras().getString("primarykey");
        categorie = intent.getExtras().getString("categorie");
        maintitle.setText(categorie);
        sendRequest();

    }


    public void sendRequest() {
        String url = "http://ccit2020.cafe24.com:8082/board_list"; //"http://ccit2020.cafe24.com:8082/login";
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
                        Log.v("TAG","반환값"+response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                List.add((jsonArray.getJSONObject(i).getString("Title")));
                                ListKey.add((jsonArray.getJSONObject(i).getString("post_num")));
                                Log.v("TAG",jsonObject.getString("Title"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(List.size()==0){
                            textview("생성된 글이 없습니다.",freeparent,"0");
                        }
                        for(int i=0; i<List.size(); i++){
                            textview(List.get(i),freeparent,ListKey.get(i));
                        }
//
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        a.appendLog(date+"/"+"E"+"/PostListActivity/" +error.toString());
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
                params.put("categorie",primarykey);
                params.put("mypost",mypost);


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
    public void textview(final String a, android.widget.LinearLayout container, final String key){
        //TextView 생성
        final TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(20);
        view1.setTextColor(Color.BLACK);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30,30,10,30);


        view1.setLayoutParams(lp);
        if (!key.equals("0")) {
            view1.setOnClickListener(new View.OnClickListener() {
                log a = new log();
                SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
                Date date1 = new Date();
                String date = format1.format(date1);
                @Override
                public void onClick(View v) {
                    Log.v("TAG",key);
                    a.appendLog(date+"/R/PostdetailActivity/"+ key);
                    Intent intent = new Intent(getApplicationContext(), PostdetailActivity.class);
                    intent.putExtra("primarykey",key);
                    startActivity(intent);
                }
            });


        }


        //부모 뷰에 추가
        container.addView(view1);
    }

}