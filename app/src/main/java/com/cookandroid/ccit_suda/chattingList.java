package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class chattingList extends AppCompatActivity {

    Button inbt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);
         inbt = (Button) findViewById(R.id.inbt);
        inbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), chatting.class);
                startActivity(intent);
            }
        }
        );

    }
}