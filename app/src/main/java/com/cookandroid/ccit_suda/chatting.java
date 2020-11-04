package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Socket;
import io.socket.client.IO;
import io.socket.emitter.Emitter;

import static io.socket.client.IO.socket;


public class chatting extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://10.0.2.2/chartEvent");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        mSocket.on("chartEvent", onMessage);
        mSocket.connect();

        btn = (Button)findViewById(R.id.sendBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emitter chat =   mSocket.emit("chartEvent", "hi");
                Log.d("send", chat.toString());
            }
        });

    }
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
                        Log.e("get", data);
                    }
                });
            }
        };
}