package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

//import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, ForcedTerminationService.class));
        Button btn1 = (Button) findViewById(R.id.Register);
        Button btn2 = (Button) findViewById(R.id.Login);
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
        String userinfo = sharedPreferences.getString("userinfo","");
        String login_check = sharedPreferences.getString("login_check","");
        Log.v("체크",login_check);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        if(!(userinfo.equals(""))&&(login_check.equals("true"))){
            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


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
                            }
                            else{
                                editor.putString("userinfo",userinfo);
                                editor.putString("login_check",String.valueOf(checkBox.isChecked()));
                                editor.commit();
                            }
                            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }


}
