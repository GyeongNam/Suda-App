package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostUploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
    }

    public void sendRequest() {
        String url = "http://10.0.2.2/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG",response.equals("1"));
                        if(response.equals("1")){
                            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                        }


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