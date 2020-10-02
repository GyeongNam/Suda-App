package com.cookandroid.ccit_suda;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class PostdetailActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private View drawerView;
    private LinearLayout container;
    private ListView postlist;
    private String imgurl;
//    ImageView imageView;

    EditText replytext;
    List<String> replylist = new ArrayList<>();
    //두줄만든거임
    ArrayList<Commentlist> commentlist = new ArrayList<>();
    CommentAdapter commentAdapter;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        replytext = (EditText) findViewById(R.id.replytext);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_postdetail);
        drawerView = (View) findViewById(R.id.drawer);
        container = (LinearLayout) findViewById(R.id.parentlayout);
        postlist = (ListView) findViewById(R.id.postlist);

        View header = getLayoutInflater().inflate(R.layout.listview_header, null, false);


        postlist.addHeaderView(header);


        ImageButton btn_open = (ImageButton) findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });
        //


        Button post = (Button) findViewById(R.id.bt_postupload);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostUploadActivity.class);
                startActivity(intent);
            }
        });

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

        InputMethodManager controlManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);


        sendRequest();
        commentAdapter = new CommentAdapter(this, commentlist);
        postlist.setAdapter(commentAdapter);


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

    public void sendRequest() {
        String url = "http://10.0.2.2/post_detail"; //"http://ccit2020.cafe24.com:8082/login";


        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                        String userinfo = sharedPreferences.getString("userinfo", "");
                        TextView username = (TextView) findViewById(R.id.username);
                        TextView title = (TextView) findViewById(R.id.Title);
                        TextView text = (TextView) findViewById(R.id.Text);
                        HashMap<Integer, String> map = new HashMap<>();
                        username.setText("환영합니다 " + userinfo + " 님");
//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);
                        commentlist.clear();
//                        postlist.invalidateViews();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
//                            JSONObject jsonObject = new JSONObject(response);
//                            Log.v("TAG",jsonArray.getString(""));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Commentlist commentlist1 = new Commentlist();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Log.v("TAG", "게시글 디테일" + jsonObject.getString("Title"));
                                //가져온 댓글 정보 넣기
                                commentlist1.setWriter(jsonObject.getString("c_writer"));
                                commentlist1.setComment(jsonObject.getString("comment"));
//                                commentlist1.setDate(jsonObject.getString("created_at"));
                                commentlist1.setNum(jsonObject.getString("c_num"));
                                //대댓글 담기영역
                                commentlist1.setParent(jsonObject.getString("parent"));
//                                commentlist1.setRecomment(jsonObject.getString("recomment"));


                                commentlist.add(commentlist1);
                                //중복검사


                                title.setText(jsonObject.getString("Title"));
                                text.setText(jsonObject.getString("Text"));
                                imgurl = "http://10.0.2.2/img/"+jsonObject.getString("image");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        commentAdapter.setItem(commentlist);
                        commentAdapter.notifyDataSetChanged();
                        Log.v("TAG",imgurl);

                        ImageView imageView = (ImageView) findViewById(R.id.imageview);
                        Picasso.get().load(imgurl).into(imageView);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "에러 ->" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
//                String userinfo = sharedPreferences.getString("userinfo", "");
                Intent intent = getIntent();
                String KEY = intent.getExtras().getString("primarykey");
                params.put("post_num", KEY);
                return params;
            }

//            public Map<String, String> getHeader() throws AuthFailureError{
//                Map<String, String> params = new HashMap<String, String >();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
        };
        request.setShouldCache(false);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
//        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }




    public void sendreply() {
        String url = "http://10.0.2.2/post_reply"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                        String userinfo = sharedPreferences.getString("userinfo", "");
                        TextView username = (TextView) findViewById(R.id.username);
//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);
                        replytext.getText().clear();
                        commentAdapter.Number = null;
                        sendRequest();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "에러 ->" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                Intent intent = getIntent();

                Log.v("TAG", "리플쓸때 number존재유무" + commentAdapter.Number);

                String reply = replytext.getText().toString();
                String KEY = intent.getExtras().getString("primarykey");
                params.put("post_num", KEY);
                params.put("reply", reply);
                params.put("writer", userinfo);
                params.put("comment_num", commentAdapter.Number);
                return params;
            }

//            public Map<String, String> getHeader() throws AuthFailureError{
//                Map<String, String> params = new HashMap<String, String >();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
        };
        request.setShouldCache(false);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
//        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }

    public void sendreply(View view) {
        sendreply();
    }

    //텍스트뷰 동적생성하기
    public void textview(final String a, android.widget.LinearLayout container) {
        //TextView 생성
        final TextView view1 = new TextView(this);
        view1.setText(a);
        view1.setTextSize(20);
        view1.setTextColor(Color.BLACK);
        Drawable drawable = getResources().getDrawable(R.drawable.round_border);
        view1.setBackground(drawable);
        //layout_width, layout_height, gravity 설정
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(30, 30, 10, 30);


        view1.setLayoutParams(lp);


        //부모 뷰에 추가
        container.addView(view1);
    }

}