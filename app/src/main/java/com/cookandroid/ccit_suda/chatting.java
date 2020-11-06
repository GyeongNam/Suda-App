package com.cookandroid.ccit_suda;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.google.gson.JsonObject;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;

import static android.provider.Settings.System.DATE_FORMAT;


public class chatting extends AppCompatActivity {
    //    String Tag = "chatting";
//    static Socket mSocket;
    private String TAG = "MainActivity";
    private Socket mSocket;
    Button sendBtn;
    LinearLayout contentFrame;
    private ScrollView scroll;
    EditText replytext;
    boolean err = false;
    private ApiInterface api;

    String msgcheck;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        sendBtn = findViewById(R.id.sendBtn);
        contentFrame = findViewById(R.id.inlayout);
        scroll = findViewById(R.id.scroll);
        replytext = findViewById(R.id.replytext);

        EchoOptions options = new EchoOptions();
        options.host = "http://ccit2020.cafe24.com:6001";
        Echo echo = new Echo(options);
        echo.connect(new EchoCallback() {
            @Override
            public void call(Object... args) {
                Log.d("Success", String.valueOf(args));
            }
        }, new EchoCallback() {
            @Override
            public void call(Object... args) {
                Log.d("Error6001", String.valueOf(args));
            }
        });

        echo.channel("laravel_database_ccit")
                .listen("chartEvent", new EchoCallback() {
                    @Override
                    public void call(Object... args) {
                        // Event thrown.
                        //JSONObject receivedData = null;
//                        try {
//                            receivedData = new JSONObject(args[0].toString());
                        Log.d("웃기지마랄라", String.valueOf(args[1]));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 msgcheck = replytext.getText().toString();

                check(msgcheck);
                if (!(msgcheck.isEmpty())) {
//                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    contentFrame = findViewById(R.id.inlayout);
                    LayoutInflater inflater = getLayoutInflater();
                    LinearLayout linearLayout = (LinearLayout) inflater.inflate( R.layout.sendmsg, null );
                    inflater.inflate(R.layout.sendmsg,contentFrame,true);
                    sendRequest();
                    scrollDown();
                } else {
                    Toast.makeText(getApplicationContext(), "대화를 입력해주세요", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    public void check(String msgcheck) {
        if (msgcheck.equals("")) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("ERR");
//            builder.setMessage("대화를 입력하세요!");
//            builder.setCancelable(false);
//            builder.setPositiveButton("확인",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            err = true;
//                        }
//                    });
//            builder.show();
        } else {
            err = false;
        }
    }
    public void scrollDown(){
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
                Log.v("쓰레드","?");
            }
        });
    }

    //        post 방식
    public void sendRequest() {
        String url = "chartEvent"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        String other = "test";
        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        params.put("sendmsg", msgcheck);
        params.put("user1", userinfo);
        params.put("user2", other);

        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                TextView userid = (TextView) findViewById(R.id.user_id1);
                TextView sendmsg = (TextView) findViewById(R.id.chat1);
                TextView chattime = (TextView) findViewById(R.id.message_time1);
                userid.setText(userinfo);
                sendmsg.setText(msgcheck);

                Date today = Calendar.getInstance().getTime();
                chattime.setText(today.toString());
                Log.v("retrofit2",String.valueOf(response.body()));
            }
            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }

}