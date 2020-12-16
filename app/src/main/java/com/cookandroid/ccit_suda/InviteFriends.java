package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class InviteFriends extends AppCompatActivity  {
    RecyclerView listView;
    TalkDatabase talkDatabse;
    User_listViewModel viewModel;
    Button chatbutton;
    EditText editText;
    ApiInterface api;
    Room_list room_list;

    private List currentSelectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        PlusroomAdapter plusroomAdapter = new PlusroomAdapter(getApplicationContext(),new PlusroomAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(String item) {
                Log.v("로그",item.toString());
                currentSelectedItems.add(item);
            }

            @Override
            public void onItemUncheck(String item) {
                currentSelectedItems.remove(item);
            }
        });
        listView = findViewById(R.id.invite_list);
        chatbutton = findViewById(R.id.button);
        editText =findViewById(R.id.roomName);
        talkDatabse = TalkDatabase.getDatabase(getApplicationContext());
        Log.v("dd",talkDatabse.talkDao().getAll_user_list().toString());
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
        viewModel.get_User_listViewModel().observe(this, new Observer<List<User_list>>(){
            public void onChanged(List<User_list> user_lists ) {
                plusroomAdapter.setData(user_lists);
            }
        });

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setAdapter(plusroomAdapter);

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PlusroomAdapter.
                if(editText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "채팅방 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else {
                    fuckRequest();
                }
            }
        });

    }

    //        post 방식
    public void fuckRequest() {
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
        String userinfo = sharedPreferences.getString("userinfo","");
        editText =findViewById(R.id.roomName);


        String url = "getroom"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        params.put("key", currentSelectedItems.toString()+","+userinfo);
        params.put("roomname", editText.getText().toString());
        params.put("userinfo", userinfo);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                try{
                    TalkDatabase talkDatabase;
                    talkDatabase = TalkDatabase.getDatabase(getApplicationContext());

                    Log.v("retrofit2", String.valueOf(response.body()));
                    JSONArray jsonArray = new JSONArray(response.body());
                    for(int i = 0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String user_name = jsonObject.getString("user");
                        String chat_room = jsonObject.getString("chat_room");
                        String room_name = jsonObject.getString("room_name");

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                room_list = new Room_list(null,user_name,chat_room,room_name);
                                talkDatabse.talkDao().insert_room_list(room_list);
                            }
                        });
                    }
                    Log.v("TAG", "zz" + jsonArray);

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String chat_room = jsonObject.getString("chat_room");
                    String room_name = jsonObject.getString("room_name");
                    Log.e("Intent",chat_room);

                    EchoOptions options = new EchoOptions();
                    Echo echo;

                    options.host = "http://ccit2020.cafe24.com:6001";
                    echo = new Echo(options);

                    echo.connect(new EchoCallback() {
                        @Override
                        public void call(Object... args) {
                            Log.d("Success", String.valueOf(args));
                        }
                    }, new EchoCallback() {
                        @Override
                        public void call(Object... args) {
                            Log.d("Error", String.valueOf(args));
                        }
                    });

                    echo.channel("laravel_database_"+chat_room)
                            .listen("chartEvent", new EchoCallback() {
                                @Override
                                public void call(Object... args) {
                                    Date now = new Date();
                                    Log.d("웃기지마랄라", String.valueOf(args[1]));
                                    String user;
                                    String message;
                                    String chat_idx;
                                    int channel;
                                    try {
                                        JSONObject jsonObject = new JSONObject(args[1].toString());
                                        chat_list list = new chat_list(jsonObject.getString("user") ,now,jsonObject.getString("message"));
                                        user = jsonObject.getString("user");
                                        message = jsonObject.getString("message");
                                        channel = Integer.parseInt(jsonObject.getString("channel"));
                                        chat_idx = jsonObject.getString("chat_idx");
                                        Talk t = new Talk(null, user, message, channel, String.valueOf(now), chat_idx);
                                        Log.v("1", String.valueOf(t));
                                        talkDatabase.talkDao().insert(t);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                    Intent intent = new Intent(getApplicationContext(), chatting.class);
                    intent.putExtra("room",chat_room);
                    startActivity(intent);
                    Log.e("Intent",intent.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
}