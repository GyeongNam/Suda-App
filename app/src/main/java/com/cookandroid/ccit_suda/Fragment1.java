package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
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

public class Fragment1 extends Fragment {

    private Context context;
    ApiInterface api;
    RecyclerView listView;
    PlusfriendAdapter plusfriendAdapter;
    private SharedPreferences sharedPreferences;
    ArrayList<plusfriend_list> plusfriend_lists_listArrayList;
    User_list user_list;
    TalkDatabase talkDatabse;
    User_listViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        listView = rootView.findViewById(R.id.ff_list);
        plusfriendAdapter = new PlusfriendAdapter(context);
        plusfriend_list plusflist = new plusfriend_list();
        talkDatabse = TalkDatabase.getDatabase(context);
        Log.v("dd",talkDatabse.talkDao().getAll_user_list().toString());
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
        viewModel.get_User_listViewModel().observe(getViewLifecycleOwner(), user ->{
            Log.v("옵저버","띠용");
            plusfriendAdapter.setData(user);
        });
        listView.setHasFixedSize(true);
        Log.v("프래그먼트 실행", "ㅇㅇㅇ");
        frienddata();
        listView.setLayoutManager(new LinearLayoutManager(context));

        listView.setAdapter(plusfriendAdapter);
        return rootView;
    }

    //        post 방식
    public void frienddata() {
        String url = "friendlist"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        sharedPreferences = this.getActivity().getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        params.put("user1", userinfo);

        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("통신성공",String.valueOf(response.body()));
                String plusfriendArray = response.body();
//                Log.v("3",String.valueOf(response.body()));
                    try {
                        JSONArray jsonArray = new JSONArray(plusfriendArray);
//                        Log.v("13", String.valueOf(jsonArray));

                        for (int i=0; i<jsonArray.length(); i++)
                        {
//                            Log.v("for문 입장?", "안녕 ");
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String a = jsonObject.getString("follow");
                            plusfriend_list plusflist = new plusfriend_list();
                            plusflist.setFollow((jsonObject.getString("follow")));
                            plusflist.setName((jsonObject.getString("follow")));
                            plusflist.setRoom(jsonObject.getString("room_idx"));
                            Log.e("e",jsonObject.getString("follow"));
                            Log.e("e",jsonObject.getString("room_idx"));

                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if(!talkDatabse.talkDao().isRowIsExist_user_list(a)){
                                        user_list = new User_list(null,a);
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
                Log.v("통신실패",String.valueOf("error : "+t.toString()));
            }
        });
    }
}
