package com.cookandroid.ccit_suda;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import com.android.volley.AuthFailureError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class MainActivity extends AppCompatActivity {
    log a = new log();
    SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
    Date date1 = new Date();
    String date = format1.format(date1);
    String androids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int status = InternetCheck.getConnectivityStatus(getApplicationContext());
        if(status == InternetCheck.TYPE_NOT_CONNECTED){
            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("오류"); //제목
            dlg.setMessage("네트워크 연결상태를 확인해 주세요."); // 메시지

//                버튼 클릭시 동작
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            dlg.show();
            Log.v("Internet","연결안됨");
        }else {
                            //fcm

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                NotificationChannel notificationChannel = new NotificationChannel("suda", "suda", NotificationManager.IMPORTANCE_DEFAULT);
//                notificationChannel.setDescription("channel description"); notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.GREEN); notificationChannel.enableVibration(true);
//                notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
//                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }


            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if(!task.isSuccessful()){
                        Log.w("fcm log", "get", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    Log.d("fcm tocken", token);
                }
            });


            //인터넷 연결상태일시에 아래코드들 실행
            startService(new Intent(this, ForcedTerminationService.class));
            androids = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            }
//            SharedPreferences sharedPreferences = getSharedPreferences("File",0);
//            String userinfo = sharedPreferences.getString("userinfo","");
//            String login_check = sharedPreferences.getString("login_check","");
//            Log.v("체크",login_check);
            pushlog();
//            if(!(userinfo.equals(""))&&(login_check.equals("true"))){
//                a.appendLog(date + "/R/login/"+ userinfo);
//                a.appendLog(date + "/M/boardActivity/0");
//                Intent intent = new Intent(getApplicationContext(), boardActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }





            Button btn1 = (Button) findViewById(R.id.Register);
            Button btn2 = (Button) findViewById(R.id.Login);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a.appendLog(date + "/M/sign_up/0");
                    Intent intent = new Intent(getApplicationContext(), sign_up.class);
                    startActivity(intent);

                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                Toast.makeText(getApplicationContext(),value+'\n'+value1,Toast.LENGTH_SHORT).show();
                    sendRequest();
                }
            });



        }


    }

    public void pushlog() {                                         // 로그파일 전송
        String android = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String url = "http://10.0.2.2/get_logfile"; //10.0.2.2 ccit2020.cafe24.com:8082
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("TAG",response);
               // new AlertDialog.Builder(getApplicationContext()).setMessage("응답:"+response).create().show();

                File file = new File("/mnt/sdcard/log.file");
                file.delete();


                a.appendLog(date + "/R/android/"+ androids);

                SharedPreferences sharedPreferences = getSharedPreferences("File",0);
                String userinfo = sharedPreferences.getString("userinfo","");
                String login_check = sharedPreferences.getString("login_check","");
                Log.v("체크",login_check);

                if(!(userinfo.equals(""))&&(login_check.equals("true"))){
                    a.appendLog(date + "/R/login/"+ userinfo);
                    a.appendLog(date + "/M/boardActivity/0");
                    Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //요청 객체에 보낼 데이터를 추가
        smpr.addFile("logfile", "/mnt/sdcard/log.file");
        smpr.addStringParam("androidid", android);
        smpr.addStringParam("datea", date.toString());
        smpr.setShouldCache(false);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(smpr);

    }
    public void sendRequest() {

        String url = "http://ccit2020.cafe24.com:8082/login"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        CheckBox checkBox = (CheckBox) findViewById(R.id.cb_save) ;
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);

                        if (response.equals("1")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            EditText userid = (EditText) findViewById(R.id.ID);
                            String userinfo = userid.getText().toString();
                            if (checkBox.isChecked()) {
                                Log.v("TAG", "체크됨");
                                editor.putString("userinfo",userinfo);
                                editor.putString("login_check",String.valueOf(checkBox.isChecked()));
                                editor.commit();
                                Log.v("TAG", String.valueOf(checkBox.isChecked()));

                                a.appendLog(date + "/R/login/"+ userinfo);
                            }
                            else{
                                editor.putString("userinfo",userinfo);
                                editor.putString("login_check",String.valueOf(checkBox.isChecked()));
                                editor.commit();

                                a.appendLog(date + "/R/login/"+ userinfo);
                            }
                            a.appendLog(date + "/M/boardActivity/0");
                            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        a.appendLog(date+"/"+"E"+"/login/" +error.toString());
                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                EditText userid = (EditText) findViewById(R.id.ID);
                String value = userid.getText().toString();
                EditText userpw = (EditText) findViewById(R.id.PW);
                String value1 = userpw.getText().toString();
                params.put("id", value);
                params.put("pw", value1);
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

    }


}
