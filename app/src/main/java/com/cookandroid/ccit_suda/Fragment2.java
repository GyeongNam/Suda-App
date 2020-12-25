package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.friendroom.FriendroomAdapter;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Fragment2 extends Fragment {

    private Context context;
    private ApiInterface api;
    RecyclerView listView;
    chatRoomAdapter chatroomadapter;
    ArrayList<chatroom_list> list_chatroomArrayList;
    TalkDatabase talkDatabse;
    User_listViewModel viewModel;
    String userinfo;
    User_list user_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        context = container.getContext();
        listView = rootView.findViewById(R.id.RecyclerView);
        SharedPreferences sharedPreferences = context.getSharedPreferences("File",0);
        userinfo = sharedPreferences.getString("userinfo","");
        FriendroomAdapter adapter = new FriendroomAdapter(context,getActivity()) ;
//        sendRequest();
        talkDatabse = TalkDatabase.getDatabase(context);
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);

        viewModel.get_Romm_listViewModel(userinfo).observe(getViewLifecycleOwner(), user ->{
            adapter.setData(user);
        });
        listView.setHasFixedSize(true);


        listView.setLayoutManager(new LinearLayoutManager(context)) ;
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.

        listView.setAdapter(adapter) ;

        return rootView;
    }
    public void sendRequest() {
        String url = "group_room"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        params.put("user", userinfo);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2",String.valueOf(response.body()));
                try {
                    JSONArray jsonArray = new JSONArray(response.body());
                    for(int i =0; i<jsonArray.length();i++){
                        JSONObject jsonObject =  jsonArray.getJSONObject(i);
                        String user = jsonObject.getString("user");
                        String chat_room = jsonObject.getString("chat_room");
//                        String room_name = jsonObject.getString("room_name");
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                if(!talkDatabse.talkDao().isRowIsExist_user_room_list(user,chat_room)){
                                    user_list = new User_list(null,user,chat_room);
                                    talkDatabse.talkDao().insert_user_list(user_list);
                                }
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
            }
        });
    }
}
