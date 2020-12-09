package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.CallbackWithRetry;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class FlistAdapter extends BaseAdapter {
    Context context;
    ArrayList<friend_list> friend_lists_itemArrayList;
    ViewHolder viewholder;
    ApiInterface api;
    SharedPreferences sharedPreferences;
    int position;
    EchoOptions options = new EchoOptions();
    Echo echo;
    private ImageView follow_bt;
    class ViewHolder {
        TextView talkuser;
        ImageView follow_bt;
    }

    public FlistAdapter(Context context, ArrayList<friend_list> friend_lists_itemArrayList) {
        this.context = context;
        this.friend_lists_itemArrayList = friend_lists_itemArrayList;
    }

    @Override
    public int getCount() {
        return friend_lists_itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return friend_lists_itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.profile,null);
            viewholder = new ViewHolder();
            viewholder.talkuser = convertView.findViewById(R.id.talkusername);
            friend_lists_itemArrayList.get(position).getName();

            Log.v("위치",friend_lists_itemArrayList.get(position).getName());
            viewholder.follow_bt = convertView.findViewById(R.id.follow_bt);
            if (friend_lists_itemArrayList.get(position).getName().equals(friend_lists_itemArrayList.get(position).getFollow())){
                Log.v("조건문실행", "실행됨");
                viewholder.follow_bt.setBackground(ContextCompat.getDrawable(context, R.drawable.unfollow));
            }
            convertView.setTag(viewholder);
        }
        else {
            viewholder = (ViewHolder)convertView.getTag();
        }
        viewholder.follow_bt = convertView.findViewById(R.id.follow_bt);
        viewholder.talkuser.setText(friend_lists_itemArrayList.get(position).getName());
        View finalConvertView = convertView;
        viewholder.follow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("포지션",(friend_lists_itemArrayList.get(position).getName()));
                Toast.makeText(context ,friend_lists_itemArrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
//                finalConvertView.setVisibility(View.GONE);
//                notifyDataSetChanged();
                followRequest(friend_lists_itemArrayList.get(position).getName(), getItemId(position), v);


            }
        });
        return convertView;
    }

    //        팔로우 버튼 통신
    public void followRequest(String name, long btpo, View bt ) {
        String url = "follows"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create( ApiInterface.class );
        HashMap<String,String> params = new HashMap<>();
        sharedPreferences = context.getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        TalkDatabase talkDatabse;
        talkDatabse = TalkDatabase.getDatabase(context);
        params.put("user1", userinfo);
        Log.v("user기록", userinfo.toString());
        params.put("user2", name);
        Log.v("talkuser기록", name);

        Log.v("버튼의 위치", String.valueOf(btpo));
        Call<String> call = api.requestPost(url,params);
        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new Callback<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                //서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능

                String data = response.body();
                Log.v("통신성공2143",String.valueOf(data));
                String[] data1 = data.split(",");

                Log.v("통신성공2143",data1[0]);
                Log.v("통신성공2144",data1[1]);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if(!talkDatabse.talkDao().isRowIsExist_user_list(name)){
                            User_list user_list = new User_list(null,name,data1[1]);
                            talkDatabse.talkDao().insert_user_list(user_list);
                        }
                        else{
                            talkDatabse.talkDao().delete_user_list(name);
                        }
                    }
                });


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

                 if(data1[0].equals("1")){
                     
                     echo.leave("laravel_database_"+data1[1].trim());
                     bt.setBackground(ContextCompat.getDrawable(context, R.drawable.follow));
                     Toast.makeText(context ,"언팔로우하셨습니다", Toast.LENGTH_SHORT).show();
                 }
                 else{

                     echo.channel("laravel_database_"+data1[1])
                             .listen("chartEvent", new EchoCallback() {
                                 @Override
                                 public void call(Object... args) {
                                     Date now = new Date();
                                     Log.d("웃기지마랄라", String.valueOf(args[1]));
                                     String qwe;
                                     String qwe1;
                                     int qwe2;
                                     try {
                                         JSONObject jsonObject = new JSONObject(args[1].toString());
                                         chat_list list = new chat_list(jsonObject.getString("user") ,now,jsonObject.getString("message"));
                                         qwe = jsonObject.getString("user");
                                         qwe1 = jsonObject.getString("message");
                                         qwe2 = Integer.parseInt(jsonObject.getString("channel"));
                                         Log.e("qwe2",jsonObject.getString("qwe2"));
                                         Talk t = new Talk(null,qwe,qwe1,qwe2,String.valueOf(now));
                                         Log.v("1",String.valueOf(t));
                                         talkDatabse.talkDao().insert(t);
                                     } catch (JSONException e) {
                                         e.printStackTrace();
                                     }
                                 }
                             });

                     bt.setBackground(ContextCompat.getDrawable(context, R.drawable.unfollow));
                     Toast.makeText(context ,"팔로우하셨습니다", Toast.LENGTH_SHORT).show();

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
