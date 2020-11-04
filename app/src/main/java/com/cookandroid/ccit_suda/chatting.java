package com.cookandroid.ccit_suda;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import net.mrbin99.laravelechoandroid.Echo;
import net.mrbin99.laravelechoandroid.EchoCallback;
import net.mrbin99.laravelechoandroid.EchoOptions;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;


public class chatting extends AppCompatActivity {
    //    String Tag = "chatting";
//    static Socket mSocket;
    private String TAG = "MainActivity";
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        EchoOptions options = new EchoOptions();
        options.host = "http://10.0.2.2:6001";
        Echo echo = new Echo(options);
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

        echo.channel("laravel_database_ccit")
                .listen("chartEvent", new EchoCallback() {
                    @Override
                    public void call(Object... args) {
                        // Event thrown.
                        //JSONObject receivedData = null;
//                        try {
//                            receivedData = new JSONObject(args[0].toString());
                        Log.d("웃기지마랄라", String.valueOf(args[1]));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                    }
                });

    }
}