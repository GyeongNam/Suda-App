package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrawerActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private TextView free_board,daily_board,nomean_board,secret_board,mypost_board;
    private LinearLayout list_parent;
    String categorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_layout);
        get_categorie_list();

    }
    @Override
    public void setContentView(int layoutResID){
        DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.toolbar_layout, null);
        FrameLayout activityContainer = (FrameLayout) fullView.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(fullView);
        list_parent = findViewById(R.id.list_parent);
        drawerLayout = (DrawerLayout) findViewById(R.id.toolbar_lay);
        drawerView = (View) findViewById(R.id.drawer);
        mypost_board = (TextView) findViewById(R.id.mypost_board);


        mypost_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                intent.putExtra("mypost",mypost_board.getText());
                intent.putExtra("categorie",mypost_board.getText());
                startActivity(intent);
            }
        });

        ImageButton btn_open = (ImageButton) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                drawerLayout.openDrawer(drawerView);
            }
        });
//
        Button post =  (Button) findViewById(R.id.bt_postupload);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostUploadActivity.class);
                startActivity(intent);
            }
        });
//
        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        Button btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                drawerLayout.closeDrawers();
                editor.remove("userinfo");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }));

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        //툴바 사용여부 결정(기본적으로 사용)
//        if(useToolbar()){
//            setSupportActionBar(toolbar);
//            setTitle("툴바예제");
//        } else {
//            toolbar.setVisibility(View.GONE);
//        }


    }
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };
    public void textview1(String a, android.widget.LinearLayout container,final String key) {
        //TextView 생성
        final TextView view1 = new TextView(this);
        view1.setText(a);
//        view1.setTextSize(20);
//        view1.setTextColor(Color.BLACK);
        view1.setGravity(Gravity.CENTER_HORIZONTAL);

        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 30, 0, 30);


        view1.setLayoutParams(lp);

        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", key);
                Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                intent.putExtra("primarykey", key);
                intent.putExtra("categorie", view1.getText());
                startActivity(intent);
            }
        });

        //부모 뷰에 추가
        container.addView(view1);
    }
    public void get_categorie_list() {
        String url = "http://10.0.2.2/get_categorie_list"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("카테고리",response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                textview1(jsonObject.getString("categorie"),list_parent,jsonObject.getString("categorie_num"));
                                Log.v("드로어액티비티",response);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "에러 ->" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        );
        request.setShouldCache(false);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }
}