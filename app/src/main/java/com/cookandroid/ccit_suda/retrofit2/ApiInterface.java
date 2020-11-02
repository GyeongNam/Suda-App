package com.cookandroid.ccit_suda.retrofit2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cookandroid.ccit_suda.R;
import com.cookandroid.ccit_suda.boardActivity;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterface {

    // base_url + "api/login" 으로 POST 통신
//    @POST("api/login")
//    Call<ResLoginData> requestPostLogin(@Body ReqLoginData reqLoginData );   // @Body : request 파라미터
    @Headers({"Connection: close"})
    @FormUrlEncoded
    @POST("{url}")
    Call<String> requestPost(@Path("url") String url, @FieldMap HashMap<String,String> params);

    // base_url + "api/users" 으로 GET 통신
    @Headers({"Connection: close"})
    @GET("{url}")
    Call<String> requestGet(@Path("url") String url);   // @Query : url에 쿼리 파라미터 추가, encoded - true
    @Headers({"Connection: close"})
    @Multipart
    @POST("{url}")
    Call<String> requestFilePost(@Path("url") String url, @PartMap HashMap<String,String> params, @Part MultipartBody.Part file);


}
//    get 방식
//    public void requestGet() {
//        String url = "서버url"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
//        api = HttpClient.getRetrofit().create( ApiInterface.class );
//        Call<String> call = api.requestGet(url);
//
//        // 비동기로 백그라운드 쓰레드로 동작
//        call.enqueue(new Callback<String>() {
//            // 통신성공 후
//            @Override
//            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                   //     서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
//                Log.v("retrofit2",response.body().toString());
//            }
//
//            // 통신실패
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                Log.v("retrofit2",String.valueOf("error : "+t.toString()));
//            }
//        });
//    }

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

