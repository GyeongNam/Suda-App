package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PostdetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);

        Intent intent = getIntent();
        String KEY = intent.getExtras().getString("primarykey");
        Log.v("TAG",KEY);
    }
}