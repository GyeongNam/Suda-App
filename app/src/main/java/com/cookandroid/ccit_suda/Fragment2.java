package com.cookandroid.ccit_suda;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Fragment2 extends Fragment {

    private Context context;
    private ApiInterface api;
    ListView listView;
    chatRoomAdapter chatroomadapter;
    ArrayList<chatroom_list> list_chatroomArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);

        context = container.getContext();
        listView = rootView.findViewById(R.id.chat_item);
        list_chatroomArrayList = new ArrayList<chatroom_list>();
        chatroomadapter = new chatRoomAdapter(context, list_chatroomArrayList);
        listView.setAdapter(chatroomadapter);
        return rootView;
    }

    //        post 방식
//    public void sendRequest() {
//        String url = "서버url"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
//        api = HttpClient.getRetrofit().create( ApiInterface.class );
//        HashMap<String,String> params = new HashMap<>();
//        params.put("key", value);
//        Call<String> call = api.requestPost(url,params);
//
//        // 비동기로 백그라운드 쓰레드로 동작
//        call.enqueue(new Callback<String>() {
//            // 통신성공 후 텍스트뷰에 결과값 출력
//            @Override
//            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//            //서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
//                Log.v("retrofit2",String.valueOf(response.body()));
//            }
//
//            // 통신실패
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
//            }
//        });
//    }
}
