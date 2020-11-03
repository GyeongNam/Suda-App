package com.cookandroid.ccit_suda;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.baoyz.widget.PullRefreshLayout;
import com.cookandroid.ccit_suda.retrofit2.ApiInterface;
import com.cookandroid.ccit_suda.retrofit2.CallbackWithRetry;
import com.cookandroid.ccit_suda.retrofit2.HttpClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


public class PostdetailActivity extends DrawerActivity {
    log a = new log();
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date1 = new Date();
    String date = format1.format(date1);
    static String input;
    private TextView post_like, post_like_button, post_writer, reply_close;
    TextView reply_tag, text_limit_indicate;
    private LinearLayout container;
    LinearLayout reply_top_layout, reply_border_layout;
    private ListView postlist;
    private String imgurl;
    InputMethodManager imm;
        ImageView Notification,refresh;
    Button del_post, md_post;
    String KEY;
    SharedPreferences sharedPreferences;
    String userinfo;
    EditText replytext;
    List<String> replylist = new ArrayList<>();
    //두줄만든거임
    ArrayList<Commentlist> commentlist = new ArrayList<>();
    CommentAdapter commentAdapter;
    private ArrayAdapter<String> adapter;
    PullRefreshLayout comment_refresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        postlist = (ListView) findViewById(R.id.postlist);
        View header = getLayoutInflater().inflate(R.layout.listview_header, null, false);
        postlist.addHeaderView(header);

        replytext = (EditText) findViewById(R.id.replytext);
//        container = (LinearLayout) findViewById(R.id.parentlayout);
        reply_top_layout = (LinearLayout) findViewById(R.id.reply_top_layout);
        reply_close = findViewById(R.id.reply_close);
        reply_tag = findViewById(R.id.reply_tag);
        reply_border_layout = findViewById(R.id.reply_border_layout);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        text_limit_indicate = findViewById(R.id.text_limit_indicate);
//        post_scrollview = findViewById(R.id.post_scrollview);
        Notification = findViewById(R.id.alert);
        refresh = findViewById(R.id.refresh);
        input = "";
        Intent intent = getIntent();
        KEY = intent.getExtras().getString("primarykey");

        comment_refresh = findViewById(R.id.comment_refresh);
        comment_refresh.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequest();
            }
        });
        postlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                int y = postlist.getFirstVisiblePosition ();
                int topEdge=postlist.getChildAt(0).getTop();
                Log.v("리스트뷰 스크롤",String.valueOf(y));
                Log.v("리스트뷰 스크롤",String.valueOf(topEdge));
                if (topEdge == 0) {
                    comment_refresh.setEnabled(true);
                }
                else {
                    comment_refresh.setEnabled(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


                post_like_button = (TextView) findViewById(R.id.post_like_button);
        post_like = (TextView) findViewById(R.id.post_like);
        post_writer = (TextView) findViewById(R.id.post_writer);
        del_post = (Button) findViewById(R.id.del_post);
        md_post = (Button) findViewById(R.id.md_post);
        ImageButton btn_open = (ImageButton) findViewById(R.id.btn_open);

        sharedPreferences = getSharedPreferences("File", 0);
        userinfo = sharedPreferences.getString("userinfo", "");
        reply_top_layout.setVisibility(View.GONE);

        sendRequest();
        replytext.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (commentAdapter.Number != null) {
                            Log.v("TAG", commentAdapter.Number);
                        } else {
                            reply_top_layout.setVisibility(View.VISIBLE);
                            reply_border_layout.setBackgroundResource(0);
                            reply_tag.setText("댓글 입력");
                            replytext.setCursorVisible(true);
                            text_limit_indicate.setText(input.length() + " / 200 글자 수");
                        }

                        break;
                    }
                }
                return false;
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"눌렀음",Toast.LENGTH_SHORT).show();

                comment_push();


            }
        });
        replytext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                input = replytext.getText().toString();
                text_limit_indicate.setText(input.length() + " / 200 글자 수");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        reply_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reply_top_layout.setVisibility(View.GONE);
                replytext.getText().clear();
                imm.hideSoftInputFromWindow(replytext.getWindowToken(), 0);
                reply_border_layout.setBackgroundResource(R.drawable.topborder);
                replytext.setCursorVisible(false);
                commentAdapter.Number = null;
                a.appendLog(date + "/U/commentadd/0");


            }
        });


        md_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostdetailActivity.this);
                builder.setTitle("게시글 수정");
                builder.setMessage("게시글 수정페이지로 이동합니다")     // 제목 부분 (직접 작성)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), postmodified.class);
                                a.appendLog(date + "/" + "M" + "/postmodified/0");
                                intent.putExtra("primarykey", KEY);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "수정페이지 이동!", Toast.LENGTH_SHORT).show(); // 실행할 코드
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                            }
                        })
                        .show();
            }
        });

        del_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostdetailActivity.this);
                builder.setTitle("게시글 삭제");
                builder.setMessage("게시글을 정말 삭제하시겠습니까?")     // 제목 부분 (직접 작성)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {      // 버튼1 (직접 작성)
                            public void onClick(DialogInterface dialog, int which) {
                                delpost();
                                a.appendLog(date + "/" + "D" + "/PostdetailActivity/" + KEY);
                                Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                                a.appendLog(date + "/" + "M" + "/boardActivity/0");
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "삭제되었습니다!", Toast.LENGTH_SHORT).show(); // 실행할 코드


                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {     // 버튼2 (직접 작성)
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소 누름", Toast.LENGTH_SHORT).show(); // 실행할 코드
                            }
                        })
                        .show();
            }
        });

        Button post = (Button) findViewById(R.id.bt_postupload);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostUploadActivity.class);
                startActivity(intent);
            }
        });
        post_like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.appendLog(date + "/U/PostdetailActivity/like");
                Toast.makeText(getApplicationContext(), "좋아요 버튼눌렀습니다.", Toast.LENGTH_SHORT).show();
                like_button();
            }
        });


        InputMethodManager controlManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);



        commentAdapter = new CommentAdapter(this, commentlist);
        postlist.setAdapter(commentAdapter);


    }


//    public void sendRequest() {
//        String url = "http://ccit2020.cafe24.com:8082/post_detail"; //"http://ccit2020.cafe24.com:8082/login";
//
//
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//
//                        TextView username = (TextView) findViewById(R.id.username);
//                        TextView title = (TextView) findViewById(R.id.Title);
//                        TextView text = (TextView) findViewById(R.id.Text);
//                        HashMap<Integer, String> map = new HashMap<>();
//                        username.setText("환영합니다 " + userinfo + " 님");
////                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("compact", response);
//                        commentlist.clear();
////                        postlist.invalidateViews();
//                        try {
//                            JSONObject data = new JSONObject(response);
//                            JSONArray jsonArray = new JSONArray(data.getString("data"));
//                            Log.v("푸시값",data.getString("comment_push"));
////                            if(data.getString("comment_push").equals("1")){
////                                Notification.setImageResource(R.drawable.cbell);
////                            }
////                            else{
////                                Notification.setImageResource(R.drawable.nbell);
////                            }
//
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                Commentlist commentlist1 = new Commentlist();
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                if(!(userinfo).equals(jsonObject.getString("writer"))) {
//                                    del_post.setVisibility(View.GONE);
//                                }
//                                if(!(userinfo).equals(jsonObject.getString("writer"))) {
//                                    md_post.setVisibility(View.GONE);
//                                }
//                                Log.v("TAG", "게시글 디테일" + jsonObject.getString("Title"));
//                                //가져온 댓글 정보 넣기
////                                Log.v("TAG",jsonObject.getString("c_activation"));
//                                commentlist1.setActivation(jsonObject.getString("c_activation"));
////                                Log.v("TAG",String.valueOf(jsonObject.getString("c_activation").equals("null")));
//                                if (!jsonObject.getString("c_activation").equals("null")) {
//                                    commentlist1.setWriter(jsonObject.getString("c_writer"));
//                                    commentlist1.setComment(jsonObject.getString("comment"));
//                                    commentlist1.setDate(jsonObject.getString("created_at").substring(0,16));
//                                    commentlist1.setNum(jsonObject.getString("c_num"));
//                                    //대댓글 담기영역
//                                    commentlist1.setParent(jsonObject.getString("parent"));
//                                    Log.v("TAG","뭐냐고 참이 아닌데 왜들어가냐");
//
//                                }
//
////                                commentlist1.setRecomment(jsonObject.getString("recomment"));
//
//
//                                commentlist.add(commentlist1);
//                                //중복검사
//
//
//                                title.setText(jsonObject.getString("Title"));
//                                text.setText(jsonObject.getString("Text"));
//                                post_writer.setText(jsonObject.getString("writer"));
//                                post_like.setText(jsonObject.getString("like"));
//                                imgurl = "http://ccit2020.cafe24.com:8082/img/"+jsonObject.getString("image");
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        commentAdapter.setItem(commentlist);
//                        commentAdapter.notifyDataSetChanged();
//                        Log.v("TAG",imgurl);
//
//                        ImageView imageView = (ImageView) findViewById(R.id.imageview);
//                        Picasso.get().load(imgurl).into(imageView);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/PostdetailActivity/" +error.toString());
//                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", error.toString());
//                    }
//                }
//
//        ) {
//
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                Intent intent = getIntent();
//                String KEY = intent.getExtras().getString("primarykey");
//                params.put("userinfo",userinfo);
//                params.put("post_num", KEY);
//                return params;
//            }
//
////            public Map<String, String> getHeader() throws AuthFailureError{
////                Map<String, String> params = new HashMap<String, String >();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
////                return params;
////            }
//        };
//        request.setShouldCache(false);
//
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        AppHelper.requestQueue.add(request);
////        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
//    }


//    public void sendreply() {
//        String url = "http://ccit2020.cafe24.com:8082/post_reply"; //"http://ccit2020.cafe24.com:8082/login";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        TextView username = (TextView) findViewById(R.id.username);
////                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", response);
//                        replytext.getText().clear();
//                        reply_top_layout.setVisibility(View.GONE);
//                        imm.hideSoftInputFromWindow(replytext.getWindowToken(), 0);
//                        reply_border_layout.setBackgroundResource(R.drawable.topborder);
//                        replytext.setCursorVisible(false);
//                        commentAdapter.Number = null;
//                        a.appendLog(date+"/"+"U"+"/PostdetailActivity/reply");
//                        sendRequest();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/PostdetailActivity/" +error.toString());
//                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", error.toString());
//                    }
//                }
//
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                Intent intent = getIntent();
//
//                Log.v("TAG", "리플쓸때 number존재유무" + commentAdapter.Number);
//
//                String reply = replytext.getText().toString();
//                String KEY = intent.getExtras().getString("primarykey");
//                params.put("post_num", KEY);
//                params.put("reply", reply);
//                params.put("writer", userinfo);
//                params.put("comment_num", commentAdapter.Number);
//                return params;
//            }
//
////            public Map<String, String> getHeader() throws AuthFailureError{
////                Map<String, String> params = new HashMap<String, String >();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
////                return params;
////            }
//        };
//        request.setShouldCache(false);
//
////        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        AppHelper.requestQueue.add(request);
////        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
//    }

    //    public void delpost() {
//        String url = "http://ccit2020.cafe24.com:8082/delete_post";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                        TextView username = (TextView) ((Activity)mContext).findViewById(R.id.username);
////                        username.setText("환영합니다 " + userinfo + " 님");
//
//
////                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", response);
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/PostdetailActivity/" +error.toString());
//                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", error.toString());
//                    }
//                }
//
//
//
//        )
//        {
//            @Override
//            protected Map<String, String> getParams() {
//            Map<String, String> params = new HashMap<String, String>();
//            Intent intent = getIntent();
//            KEY = intent.getExtras().getString("primarykey");
//            params.put("post_num", KEY);
//            params.put("writer", userinfo);
//            return params;
//        }
//
////            public Map<String, String> getHeader() throws AuthFailureError{
////                Map<String, String> params = new HashMap<String, String >();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
////                return params;
////            }
//        };
//        request.setShouldCache(false);
//        AppHelper.requestQueue.add(request);
//    }
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

    //    public void like_button(){
//        String url = "http://ccit2020.cafe24.com:8082/post_like"; //"http://ccit2020.cafe24.com:8082/login";
//        StringRequest request = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.v("TAG", response);
//                        sendRequest();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        a.appendLog(date+"/"+"E"+"/PostdetailActivity/" +error.toString());
//                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
//                        Log.v("TAG", error.toString());
//                    }
//                }
//
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                Intent intent = getIntent();
//                KEY = intent.getExtras().getString("primarykey");
//                params.put("post_num", KEY);
//                params.put("writer", userinfo);
//                return params;
//            }
//
////            public Map<String, String> getHeader() throws AuthFailureError{
////                Map<String, String> params = new HashMap<String, String >();
////                params.put("Content-Type", "application/x-www-form-urlencoded");
////                return params;
////            }
//        };
//        request.setShouldCache(false);
//        AppHelper.requestQueue.add(request);
//
//
//    }
    //댓글 알림설정
    public void comment_push() {
        String url = "http://ccit2020.cafe24.com:8082/comment_push"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("TAG", response);
                        Drawable temp = Notification.getDrawable();
                        Drawable temp1 = getResources().getDrawable(R.drawable.nbell);
                        Bitmap tmpBitmap = ((BitmapDrawable)temp).getBitmap();
                        Bitmap tmpBitmap1 = ((BitmapDrawable)temp1).getBitmap();
                        if(tmpBitmap.equals(tmpBitmap1)){
                            Notification.setImageResource(R.drawable.cbell);
                            Toast.makeText(getApplicationContext(),"댓글 푸시 알림이 설정되었습니다.",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Notification.setImageResource(R.drawable.nbell);
                            Toast.makeText(getApplicationContext(),"댓글 푸시 알림이 취소되었습니다.",Toast.LENGTH_SHORT).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        a.appendLog(date + "/" + "E" + "/PostdetailActivity/" + error.toString());
                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("KEY", KEY);
                params.put("userinfo", userinfo);
                return params;
            }

//            public Map<String, String> getHeader() throws AuthFailureError{
//                Map<String, String> params = new HashMap<String, String >();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);


    }

    public void sendRequest() {
        String url = "post_detail"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        Intent intent = getIntent();
        String KEY = intent.getExtras().getString("primarykey");
        params.put("userinfo", userinfo);
        params.put("post_num", KEY);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                TextView username = (TextView) findViewById(R.id.username);
                TextView title = (TextView) findViewById(R.id.Title);
                TextView text = (TextView) findViewById(R.id.Text);
                HashMap<Integer, String> map = new HashMap<>();
                username.setText("환영합니다 " + userinfo + " 님");
//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                Log.v("compact", response.body().toString());
                commentlist.clear();
//                        postlist.invalidateViews();
                try {
                    JSONObject data = new JSONObject(response.body());
                    JSONArray jsonArray = new JSONArray(data.getString("comment"));
                    JSONArray jsonArray1 = new JSONArray(data.getString("data"));
                    JSONObject postdata = jsonArray1.getJSONObject(0);
                    Log.v("푸시값", data.getString("comment_push"));
                            if(data.getString("comment_push").equals("1")){
                                Notification.setImageResource(R.drawable.cbell);
                            }
                            else{
                                Notification.setImageResource(R.drawable.nbell);
                            }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Commentlist commentlist1 = new Commentlist();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        //가져온 댓글 정보 넣기
//                                Log.v("TAG",jsonObject.getString("c_activation"));
                        commentlist1.setActivation(jsonObject.getString("c_activation"));
//                                Log.v("TAG",String.valueOf(jsonObject.getString("c_activation").equals("null")));
                        if (!jsonObject.getString("c_activation").equals("null")) {
                            commentlist1.setWriter(jsonObject.getString("c_writer"));
                            commentlist1.setComment(jsonObject.getString("comment"));
                            commentlist1.setDate(jsonObject.getString("created_at").substring(0, 16));
                            commentlist1.setNum(jsonObject.getString("c_num"));
                            //대댓글 담기영역
                            commentlist1.setParent(jsonObject.getString("parent"));
                            Log.v("TAG", "뭐냐고 참이 아닌데 왜들어가냐");

                        }

//                                commentlist1.setRecomment(jsonObject.getString("recomment"));


                        commentlist.add(commentlist1);
                        //중복검사

                    }
                    if (!(userinfo).equals(postdata.getString("writer"))) {
                        del_post.setVisibility(View.GONE);
                    }
                    if (!(userinfo).equals(postdata.getString("writer"))) {
                        md_post.setVisibility(View.GONE);
                    }
                    title.setText(postdata.getString("Title"));
                    text.setText(postdata.getString("Text"));
                    post_writer.setText(postdata.getString("writer"));
                    post_like.setText(postdata.getString("like"));
                    imgurl = "http://ccit2020.cafe24.com:8082/img/" + postdata.getString("image");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                commentAdapter.setItem(commentlist);
                commentAdapter.notifyDataSetChanged();
//                Log.v("TAG",imgurl);

                ImageView imageView = (ImageView) findViewById(R.id.imageview);
                Picasso.get().load(imgurl).into(imageView);
                comment_refresh.setRefreshing(false);
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
                comment_refresh.setRefreshing(false);
            }
        });
    }

    public void sendreply() {
        String url = "post_reply"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        Intent intent = getIntent();
        Log.v("TAG", "리플쓸때 number존재유무" + commentAdapter.Number);
        if (commentAdapter.Number == null) {
            commentAdapter.Number = "null";
        }
        String reply = replytext.getText().toString();
        String KEY = intent.getExtras().getString("primarykey");
        params.put("post_num", KEY);
        params.put("reply", reply);
        params.put("writer", userinfo);
        params.put("comment_num", commentAdapter.Number);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2", response.body().toString());
                TextView username = (TextView) findViewById(R.id.username);
//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                replytext.getText().clear();
                reply_top_layout.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(replytext.getWindowToken(), 0);
                reply_border_layout.setBackgroundResource(R.drawable.topborder);
                replytext.setCursorVisible(false);
                commentAdapter.Number = null;
                a.appendLog(date + "/" + "U" + "/PostdetailActivity/reply");
                sendRequest();
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
                a.appendLog(date + "/" + "E" + "/sign_up/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delpost() {
        String url = "delete_post"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        Intent intent = getIntent();
        KEY = intent.getExtras().getString("primarykey");
        params.put("post_num", KEY);
        params.put("writer", userinfo);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2", String.valueOf(response.body()));
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
                a.appendLog(date + "/" + "E" + "/sign_up/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void like_button() {
        String url = "post_like"; //ex) 요청하고자 하는 주소가 http://10.0.2.2/login 이면 String url = login 형식으로 적으면 됨
        api = HttpClient.getRetrofit().create(ApiInterface.class);
        HashMap<String, String> params = new HashMap<>();
        Intent intent = getIntent();
        KEY = intent.getExtras().getString("primarykey");
        params.put("post_num", KEY);
        params.put("writer", userinfo);
        Call<String> call = api.requestPost(url, params);

        // 비동기로 백그라운드 쓰레드로 동작
        call.enqueue(new CallbackWithRetry<String>() {
            // 통신성공 후 텍스트뷰에 결과값 출력
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//서버에서 넘겨주는 데이터는 response.body()로 접근하면 확인가능
                Log.v("retrofit2", String.valueOf(response.body()));
                sendRequest();
            }

            // 통신실패
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
                Log.v("retrofit2", String.valueOf("error : " + t.toString()));
                a.appendLog(date + "/" + "E" + "/sign_up/" + t.toString());
                Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();


    }
    //edittext 아웃포커스
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}