package com.cookandroid.ccit_suda;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class plusfriendAdapter extends BaseAdapter {
    Context context;
    ArrayList<plusfriend_list> list_plusfriendList;
    FriendViewHolder friendViewHolder;



    public plusfriendAdapter(Context context, ArrayList<plusfriend_list> list_plusfriendList) {
        this.context = context;
        this.list_plusfriendList = list_plusfriendList;


    }

    @Override
    public int getCount() {
        return this.list_plusfriendList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list_plusfriendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friendcard, null);
            friendViewHolder = new FriendViewHolder();
            friendViewHolder.plusname = (TextView) convertView.findViewById(R.id.plusname);
            friendViewHolder.chatBtn = (Button) convertView.findViewById(R.id.chatbtn);
            //아래 내용 없어서 팅겼었음
            convertView.setTag(friendViewHolder);
            //

        } else {
            friendViewHolder = (FriendViewHolder) convertView.getTag();
        }

        friendViewHolder.plusname.setText(list_plusfriendList.get(position).getName());
        friendViewHolder.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chatting.class);
//                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("room",list_plusfriendList.get(position).getRoom());
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }
//        public void sendRequest() {
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
////서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
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
class FriendViewHolder {

    TextView plusname;
    Button chatBtn;

}