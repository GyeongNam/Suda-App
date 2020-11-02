package com.cookandroid.ccit_suda;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ForcedTerminationService extends Service {

    public IBinder onBind(Intent intent){
        return null;
    }
    //자동로그인 체크 안했을시 로그인 데이터 파기
    public void onDestroy(){
        super.onDestroy();

        Log.v("TAG","앱이 종료됨");

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String login_check = sharedPreferences.getString("login_check","");
        if((login_check.equals("false"))){
            Log.v("백그라운드","돌아라");
            logout3();
        }
        Log.e("Error","onTaskRemoved - 강제 종료 " + rootIntent);
        stopSelf(); //서비스 종료
    }
    public void logout3() {

        String url = "http://ccit2020.cafe24.com:8082/api/logout"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("백그라운드",response);
                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                params.put("id", userinfo);
//                params.put("id","test");
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }



}
