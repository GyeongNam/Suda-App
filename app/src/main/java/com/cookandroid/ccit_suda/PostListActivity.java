package com.cookandroid.ccit_suda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.CallbackWithRetry;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;

public class PostListActivity extends DrawerActivity {
    TextView maintitle;
    private ImageView alarm;
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
    ScrollView post_list_scrollview;
    PullRefreshLayout post_list_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        freeparent = findViewById(R.id.freeparent);
        post_list_scrollview = findViewById(R.id.post_list_scrollview);
        post_list_refresh = findViewById(R.id.post_list_refresh);
        maintitle = (TextView)findViewById(R.id.maintitle);
        alarm = (ImageView)findViewById(R.id.alarm);
        Intent intent = getIntent();
        mypost = intent.getExtras().getString("mypost");
//        Log.v("mypost",mypost.toString());
        primarykey = intent.getExtras().getString("primarykey");
        categorie = intent.getExtras().getString("categorie");
        maintitle.setText(categorie);
        sendRequest();
        alarm = (ImageView)findViewById(R.id.alarm);
        alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                alarmRequest();
            }
        });
        post_list_scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = post_list_scrollview.getScrollY(); //for verticalScrollView
                Log.v("스크롤",String.valueOf(scrollY));
                if (scrollY == 0)
                    post_list_refresh.setEnabled(true);
                else
                    post_list_refresh.setEnabled(false);
            }
        });

        post_list_refresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });


    }
//    public void alarmRequest() {
//        String url = "http://ccit2020.cafe24.com:8082/alarm";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.v("TAGal", response);
//                        Drawable temp = alarm.getDrawable();
//                        Drawable temp1 = getResources().getDrawable(R.drawable.nbell);
//                        Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
//                        Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();
//                        if(tmpBitmap.equals(tmpBitmap1)){
//                            alarm.setImageResource(R.drawable.cbell);
//                            Toast.makeText(getApplicationContext(), categorie +"  알림이 설정되었습니다.",Toast.LENGTH_SHORT).show();
//
//                        }
//                        else{
//                            alarm.setImageResource(R.drawable.nbell);
//                            Toast.makeText(getApplicationContext(),categorie +"  알림이 취소되었습니다.",Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/PostListActivity/" +error.toString());
//                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", error.toString());
//                    }
//                }
//
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
//                String userinfo = sharedPreferences.getString("userinfo", "");
//                params.put("user_id", userinfo);
//                params.put("pkey",primarykey);
//
//                return params;
//            }
//
//
//        };
//        request.setShouldCache(false);
//
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        AppHelper.requestQueue.add(request);
//        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
//    }

//    public void sendRequest() {
//        String url = "http://10.0.2.2/board_list"; //"http://ccit2020.cafe24.com:8082/login";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
//                        String userinfo = sharedPreferences.getString("userinfo", "");
//                        TextView username = (TextView) findViewById(R.id.username);
//                        username.setText("환영합니다 " + userinfo + " 님");
//                        Log.v("TAG","반환값"+response);
//                        try {
//                            JSONObject pdata = new JSONObject(response);
//                            JSONArray jsonArray = new JSONArray(pdata.getString("data"));
//                            Log.v("pdata", "pdata"+ pdata.getString("pdata"));
//                            if(pdata.getString("pdata").equals("1")){
//                                alarm.setImageResource(R.drawable.cbell);
//                            }
//                            else{
//                                alarm.setImageResource(R.drawable.nbell);
//                            }
//                            for(int i=0; i<jsonArray.length(); i++){
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                List.add((jsonArray.getJSONObject(i).getString("Title")));
//                                ListKey.add((jsonArray.getJSONObject(i).getString("post_num")));
//                                Log.v("TAG",jsonObject.getString("Title"));
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if(List.size()==0){
//                            textview("생성된 글이 없습니다.",freeparent,"0");
//                        }
//                        for(int i=0; i<List.size(); i++){
//                            textview(List.get(i),freeparent,ListKey.get(i));
//                        }
////
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/PostListActivity/" +error.toString());
//                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", error.toString());
//                    }
//                }
//
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
//                String userinfo = sharedPreferences.getString("userinfo", "");
//                params.put("userid", userinfo);
//                params.put("categorie",primarykey);
//
//                return params;
//            }
//
////            public Map<String, String> getHeader() throws AuthFailureError{
////                Map<String, String> params = new HashMap<String, String >();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
////                return params;
////            }
//        };
//        request.setShouldCache(false);
//
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        AppHelper.requestQueue.add(request);
//        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
//    }
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
    public void sendRequest() {
        String url = "board_list"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("userid", userinfo);
        params.put("categorie",primarykey);
        params.put("mypost",mypost);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                post_list_refresh.setRefreshing(false);
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                TextView username = (TextView) findViewById(R.id.username);
                username.setText("환영합니다 " + userinfo + " 님");
                freeparent.removeAllViews();
                List.clear();
                ListKey.clear();
                Log.v("TAG","반환값"+response.body().toString());
                try {
                    JSONObject pdata = new JSONObject(response.body());
                    JSONArray jsonArray = new JSONArray(pdata.getString("data"));
                    Log.v("pdata", "pdata"+ pdata.getString("pdata"));
                    if(pdata.getString("pdata").equals("1")){
                        alarm.setImageResource(R.drawable.cbell);
                    }
                    else{
                        alarm.setImageResource(R.drawable.nbell);
                    }
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

            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
                a.appendLog(date + "/" + "E" + "/sign_up/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                post_list_refresh.setRefreshing(false);
            }
        });
    }
    public void alarmRequest() {
        String url = "alarm"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("user_id", userinfo);
        params.put("pkey",primarykey);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("TAGal", response.body().toString());
                Drawable temp = alarm.getDrawable();
                Drawable temp1 = getResources().getDrawable(R.drawable.nbell);
                Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
                Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();
                if(tmpBitmap.equals(tmpBitmap1)){
                    alarm.setImageResource(R.drawable.cbell);
                    Toast.makeText(getApplicationContext(), categorie +"  알림이 설정되었습니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    alarm.setImageResource(R.drawable.nbell);
                    Toast.makeText(getApplicationContext(),categorie +"  알림이 취소되었습니다.",Toast.LENGTH_SHORT).show();
                }
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
                a.appendLog(date + "/" + "E" + "/sign_up/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}