package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Socket;
import io.socket.client.IO;

import static io.socket.client.IO.socket;


public class chatting extends AppCompatActivity {
    String Tag = "chatting";
    static Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        //IO.socket 메소드는 은 저 URL 을 토대로 클라이언트 객체를 Return 합니다.
        try {
            mSocket = IO.socket("http://10.0.2.2/chartEvent");
            mSocket.on(Socket.EVENT_CONNECT, (Object... objects) -> {
            JsonObject preJsonObject = new JsonObject();
            preJsonObject.addProperty("roomName", "myroom");
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(preJsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("joinRoom",jsonObject);
        }).on("recMsg", (Object... objects) -> {
            JsonParser jsonParsers = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParsers.parse(objects[0] + "");
            runOnUiThread(()->{
                Log.v("comment",jsonObject.get("comment").getAsString());
            });
        });
        mSocket.connect();
    } catch(Exception e) {
        Log.e("mSocket",e.toString() );
    }

    }
}