package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Random;



public class sign_up extends AppCompatActivity {
    boolean err = false;
    boolean iderr = true;
    String Randomnum = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button back = (Button) findViewById(R.id.bt_back);   // 돌아가기 버튼
        final Button sign_up = (Button) findViewById(R.id.bt_signup);    // 회원가입 버튼
        Button id_check = (Button) findViewById(R.id.bt_idcheck);    // 아이디 중복확인
        Button sms_send = (Button) findViewById(R.id.bt_smssend);    // 인증번호 전송
        Button sms_check = (Button) findViewById(R.id.bt_smscheck);    // 인증번호 확인

        final EditText inputid = (EditText) findViewById(R.id.et_id); // 아이디 입력창
        final EditText inputpwd = (EditText) findViewById(R.id.et_pw);  // 비번 입력창
        final EditText inputrepwd = (EditText) findViewById(R.id.et_repw);   // 비번확인 입력창
        final EditText intputphone = (EditText) findViewById(R.id.et_phone);   // 전화번호 입력창
        final EditText intputsmsnum = (EditText) findViewById(R.id.et_smsnumber);   // 인증번호 입력창




        back.setOnClickListener(new View.OnClickListener(){         // 뒤로가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {         // 회원가입 버튼
            @Override
            public void onClick(View view) {
                String id = inputid.getText().toString();
                String pwd = inputpwd.getText().toString();
                String phone = intputphone.getText().toString();
                String smsnum= intputsmsnum.getText().toString();
                String repwd = inputrepwd.getText().toString();

                check(id, pwd, repwd, phone, smsnum);
                if(iderr){
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 다시해주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!err){
                    register();               // 아래에 함수있고 회원가입 통신부분
                    }
                }
            }
        });
        id_check.setOnClickListener(new View.OnClickListener() {         // 아이디 중복확인 버튼
            @Override
            public void onClick(View view) {
                idcheck();
            }
        });

        sms_send.setOnClickListener(new View.OnClickListener() {         // 인증번호 전송 버튼
            @Override
            public void onClick(View view) {
                String phone = intputphone.getText().toString();
                if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)){
                    Toast.makeText(getApplicationContext(), "휴대전화 번호를 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    err = true;
                }
                else {
                    String Randomnum2 = "0";
                    for (int i = 0; i<5; i++){
                        int  j = (int) (Math.random() * 10);
                        Randomnum2 = Randomnum2.concat(Integer.toString(j));
                    }
                    Randomnum = Randomnum2;
                    Certification();            // 아래 함수 있고 인증번호 보내는 서버통신
                    Toast.makeText(getApplicationContext(), "문자로 인증번호를 발송했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sms_check.setOnClickListener(new View.OnClickListener() {         // 인증번호 확인 버튼
            @Override
            public void onClick(View view) {
                String smsnum= intputsmsnum.getText().toString();
                if(Randomnum.equals(smsnum)){
                    Toast.makeText(getApplicationContext(), "전화번호가 인증되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "인증번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    err = true;
                }
            }
        });


    }
    public void check(String id, String pwd, String repwd, String phone, String smsnum){    //유효성 검사
        if(id.equals("") || pwd.equals("") || repwd.equals("") || phone.equals("") || smsnum.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage("공백인 항목이 있습니다. 공백은 입력할 수 없습니다.");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        }
        else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pwd)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage("비밀번호는 8~20자리 영어와 숫자, 특수문자 조합으로 이루어져야 합니다.");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        }
        else if(!pwd.equals(repwd)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage("비밀번호와 비밀번호 확인이 다릅니다.");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        }
        else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage("휴대전화 번호를 다시 확인해 주세요");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        }
        else if(Randomnum.equals("0")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage(" 휴대전화 인증번호를 다시 확인해 주세요");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        }
        else if(!smsnum.equals(Randomnum)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage("인증번호가 다릅니다.");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        }
        else {
            err = false;
        }
    }
    public void idcheck(){
        String url = "http://ccit2020.cafe24.com:8082/idcheck"; // id 중복확인하는 링크
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG",response.equals("1"));
                        if(response.equals("1")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(sign_up.this);
                            builder.setTitle("SUCCESS");
                            builder.setMessage("사용가능한 아이디입니다.");
                            builder.setCancelable(false);

                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            iderr = false;
                                        }
                                    });
                            builder.show();
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(sign_up.this);
                            builder.setTitle("ERR");
                            builder.setMessage("이미 사용중인 아이디 입니다.");
                            builder.setCancelable(false);

                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            iderr = true;
                                        }
                                    });
                            builder.show();
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
                String id = inputid.getText().toString();
                params.put("id", id);
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
    public void register() {
        String url = "http://10.0.2.2/signup"; // 회원가입하는 링크
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
    public void Certification() {       // 인증번호 보내기
        String url = "http://10.0.2.2/SendMessage"; // 인증번호 보내는 링크
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG",response.equals("1"));
                        if(response.equals("1")){
                            Toast.makeText(getApplicationContext(), "인증번호 전송 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "인증번호 전송 실패", Toast.LENGTH_SHORT).show();
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
                final EditText intputphone = (EditText) findViewById(R.id.et_phone);   // 전화번호 입력창
                String phone = intputphone.getText().toString();
                params.put("phone", phone);
                params.put("random", Randomnum);
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