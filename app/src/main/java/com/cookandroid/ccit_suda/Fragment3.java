package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Fragment3 extends Fragment {

    private Context context;
    private ApiInterface api;
    private Button Search_bt;
    private SharedPreferences sharedPreferences;
    private EditText search_text;
    private String stext;

    ViewGroup rootView;
    ListView listView;
    FlistAdapter flistAdapter;
    ArrayList<friend_list> friend_listArrayList;
    ArrayList<String> friendList = new ArrayList<>();
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
         rootView = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        friend_listArrayList  = new ArrayList<friend_list>();
        listView = rootView.findViewById(R.id.friend_list);
        flistAdapter = new FlistAdapter(context,friend_listArrayList);
        listView.setAdapter(flistAdapter);

        search_text = rootView.findViewById(R.id.search_text);
        Search_bt = rootView.findViewById(R.id.Search_bt);
        Search_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friend_listArrayList.clear();
               stext = search_text.getText().toString();
                SharedPreferences sharedPreferences = context.getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");

                friend_list flist = new friend_list(stext);
                findRequest();
//                friend_listArrayList.add(flist);
                flistAdapter.notifyDataSetChanged();

            }
        });
        return rootView;

    }

    public void findRequest(){
        String url = "search"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        sharedPreferences = this.getActivity().getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();

        params.put("user1", userinfo);
        params.put("user2", stext);
        Call<String> call = api.requestPost(url,params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("1",String.valueOf(response.body()));
                Log.v("2",String.valueOf(response));

                 String flistArray = response.body();
                Log.v("3",String.valueOf(response.body()));
                if (String.valueOf(flistArray).equals("[]")) {
                    Toast.makeText(context ,"일치하는 회원이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        JSONArray jsonArray = new JSONArray(flistArray);
                        Log.v("13", String.valueOf(jsonArray));
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            Log.v("4", String.valueOf(jsonObject));
                            friendList.add(jsonObject.getString("id"));
                            Log.v("4", String.valueOf(jsonObject.getString("id")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
