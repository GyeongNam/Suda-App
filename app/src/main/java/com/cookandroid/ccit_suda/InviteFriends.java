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
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    User_list user_list;

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
        String chat_room;
        String room_name;
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
        String userinfo = sharedPreferences.getString("userinfo","");
        editText =findViewById(R.id.roomName);

        String url = "getroom"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        params.put("key", currentSelectedItems.toString());
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
                    Log.v("retrofit2", String.valueOf(response.body()));
                    JSONArray jsonArray = new JSONArray(response.body());
                    Log.v("TAG", "zz" + jsonArray);

                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String chat_room = jsonObject.getString("chat_room");
                    String room_name = jsonObject.getString("room_name");
                    Log.e("Intent",chat_room);
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            user_list = new User_list(null,null,chat_room,room_name);
                            talkDatabse.talkDao().insert_user_list(user_list);
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