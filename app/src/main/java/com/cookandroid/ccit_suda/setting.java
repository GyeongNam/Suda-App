package com.cookandroid.ccit_suda;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.CallbackWithRetry;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class setting extends DrawerActivity {
    log a = new log();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date1 = new Date();
    String date = format1.format(date1);
    String keyword;
    private LinearLayout list_setting, layoutsetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getkeyword();

        layoutsetting = (LinearLayout) findViewById(R.id.settinglayout);
        list_setting = (LinearLayout) findViewById(R.id.list_setting);
        CompoundButton switch1 = (CompoundButton) findViewById(R.id.switch1);
        final EditText ktext = (EditText) findViewById(R.id.editText);
        Button kbutton = (Button) findViewById(R.id.kbutton);
        layoutsetting.setBackgroundResource(R.drawable.topborder);

        kbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = ktext.getText().toString();
                if (keyword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "공백은 키워드로 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else if (keyword.length() < 3) {
                    Toast.makeText(getApplicationContext(), "키워드는 3글자 이상은 되어야 합니다.", Toast.LENGTH_SHORT).show();
                } else if (list_setting.getChildCount() > 2) {
                    Toast.makeText(getApplicationContext(), "키워드는 최대 3개까지 입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    keywordadd(keyword);
                    ktext.setText("");
                    AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                    builder.setTitle("키워드 추가 완료");
                    builder.setMessage(keyword + " 가 키워드로 추가되었습니다.");
                    builder.setCancelable(false);

                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                }
            }
        });

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alsetting(1);
                } else {
                    alsetting(0);
                }
            }
        });
    }

    public void textview2(final String aa, android.widget.LinearLayout container, final String key) {
        log b = new log();
        Date dateb = new Date();
        //TextView 생성
        final TextView view2 = new TextView(this);
        view2.setText(aa);
        view2.setTextSize(20);
        view2.setTextColor(Color.BLACK);
        view2.setGravity(Gravity.CENTER_HORIZONTAL);
        view2.setBackgroundResource(R.drawable.edge);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 30, 0, 30);

        view2.setLayoutParams(lp);

        view2.setOnClickListener(new View.OnClickListener() {
            log a = new log();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = new Date();
            String date = format1.format(date1);

            @Override
            public void onClick(View v) {
                a.appendLog(date + "/D/keyword/0");
                Log.v("TAG", key);

                AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
                builder.setTitle("키워드 삭제");
                builder.setMessage(aa + "를 정말 삭제하시겠습니까?")     // 제목 부분 (직접 작성)
                        .setPositiveButton("취소", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("확인", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                            public void onClick(DialogInterface dialog, int which) {
                                removekeyword(aa);
                            }
                        })
                        .show();

            }
        });
        //부모 뷰에 추가
        container.addView(view2);
    }
//    public void removekeyword(final String keyword){
//        String url = "http://ccit2020.cafe24.com:8082/removekeyword"; //"http://ccit2020.cafe24.com:8082/login";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("removeTAGSETTING", response);
//                        list_setting.removeAllViews();
//                        getkeyword();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/setting/" +error.toString());
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
//                params.put("keyword", keyword);
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
//    } // 키워드 삭제하기

//    public void getkeyword(){
//        String url = "http://ccit2020.cafe24.com:8082/getkeyword"; //"http://ccit2020.cafe24.com:8082/login";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                textview2(jsonObject.getString("text"), list_setting, jsonObject.getString("k_num"));
//                                CompoundButton switch1 = (CompoundButton) findViewById(R.id.switch1);
//                                if(jsonObject.getInt("push") == 1){
//                                    switch1.setChecked(true);
//                                }
//                                else {
//                                    switch1.setChecked(false);
//                                }
//                                Log.v("getkeyword", response);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
////                            Log.v("getkeyword", response);
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
//                params.put("userid", userinfo);
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
//    } // 키워드 리스트 가겨오기

//    public void alsetting(String userinfo, final int onoff) {
//        String url = "http://ccit2020.cafe24.com:8082/alsetting"; //"http://ccit2020.cafe24.com:8082/login";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("TAGSETTING", response);
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
//                params.put("onoff", String.valueOf(onoff));
//
//                return params;
//            }
//        };
//        request.setShouldCache(false);
//        AppHelper.requestQueue.add(request);
//
//    } // 알림 on off

    public void keywordadd(final String keyword) {
        String url = "http://ccit2020.cafe24.com:8082/keywordadd"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAGSETTING", response);
                        list_setting.removeAllViews();
                        getkeyword();

//                        Intent intent = getIntent();
//                        finish();
//                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        a.appendLog(date + "/" + "E" + "/PostListActivity/" + error.toString());
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
                params.put("keyword", keyword);

                return params;
            }
        };
        request.setShouldCache(false);

        AppHelper.requestQueue.add(request);
    } // 키워드 추가

    public void removekeyword(final String keyword) { // 키워드 삭제하기
        String url = "removekeyword"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("userid", userinfo);
        params.put("keyword", keyword);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

                Log.d("removeTAGSETTING", response.body().toString());
                list_setting.removeAllViews();
                getkeyword();
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
                a.appendLog(date + "/" + "E" + "/sign_up/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getkeyword() { // 키워드 리스트 가겨오기
        String url = "getkeyword"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
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
//                Log.v("retrofit2",String.valueOf(response.body()));
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        textview2(jsonObject.getString("text"), list_setting, jsonObject.getString("k_num"));
                        CompoundButton switch1 = (CompoundButton) findViewById(R.id.switch1);
                        if (jsonObject.getInt("push") == 1) {
                            switch1.setChecked(true);
                        } else {
                            switch1.setChecked(false);
                        }
                        Log.v("getkeyword", response.body());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                            Log.v("getkeyword", response);
                }
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
            }
        });
    }

    public void alsetting(final int onoff) { // 알림 on off
        String url = "alsetting"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                params.put("userid", userinfo);
                params.put("onoff", String.valueOf(onoff));
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.d("TAGSETTING", response.body());
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) { super.onFailure(call,t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
            }
        });

    }
}