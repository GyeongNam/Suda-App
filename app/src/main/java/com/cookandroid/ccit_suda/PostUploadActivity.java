package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PostUploadActivity extends AppCompatActivity {
    boolean err = false;
    boolean empty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        Button back = (Button) findViewById(R.id.bt_backlist);   //목록으로 돌아가기 버튼
        Button upload = (Button) findViewById(R.id.bt_upload);  //글 작성 업로드 버튼
        ImageButton image = (ImageButton) findViewById(R.id.imageButton);  //이미지 업로드 버튼
        Spinner contentlist = (Spinner) findViewById(R.id.spinner);  //게시판 카테고리 설정


        final EditText inputname = findViewById(R.id.et_postname);  //게시글 제목입력
        final EditText inputpost = findViewById(R.id.et_postcontent);  //게시글 내용 입력

        back.setOnClickListener(new View.OnClickListener(){         // 뒤로가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {         // 글 작성 버튼
            @Override
            public void onClick(View view) {
                String name = inputname.getText().toString();
                String content = inputpost.getText().toString();

                //check(name, content);
                if(empty){
                    Toast.makeText(getApplicationContext(), "필수 입력 항목을 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!err){
                        postupload();               // 아래에 함수있고 회원가입 통신부분
                    }
                }
            }
            public void postupload() {
                String url = "http://10.0.2.2/add_post"; // 회원가입하는 링크
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG",response.equals("1"));
                                if(response.equals("1")){
                                    Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
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
                        final EditText inputid = (EditText) findViewById(R.id.et_id); // 아이디 입력창
                        final EditText inputpwd = (EditText) findViewById(R.id.et_pw);  // 비번 입력창
                        final EditText intputphone = (EditText) findViewById(R.id.et_phone);   // 전화번호 입력창
                        String id = inputid.getText().toString();
                        String pwd = inputpwd.getText().toString();
                        String phone = intputphone.getText().toString();
                        params.put("id", id);
                        params.put("password", phone);
                        params.put("phone", pwd);
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
        });
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