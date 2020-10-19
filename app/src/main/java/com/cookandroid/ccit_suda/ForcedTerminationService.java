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

public class ForcedTerminationService extends Service {

    public IBinder onBind(Intent intent){
        return null;
    }
    //자동로그인 체크 안했을시 로그인 데이터 파기
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String login_check = sharedPreferences.getString("login_check","");
        if((login_check.equals("false"))){
            editor.clear();
            editor.commit();

        }
        Log.v("TAG","앱이 종료됨");
    }


}
