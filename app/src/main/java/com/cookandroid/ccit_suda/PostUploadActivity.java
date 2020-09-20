package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PostUploadActivity extends AppCompatActivity {

    boolean err = false;
    boolean chk;
    String Randomnum = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        Button back = (Button) findViewById(R.id.bt_backlist);   // 돌아가기 버튼
        final Button upload = (Button) findViewById(R.id.bt_upload);    // 글 작성 버튼

        final EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_cate);  // 글 카테고리 입력창
        final EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);   // 글 내용 입력창

        back.setOnClickListener(new View.OnClickListener() {         // 뒤로가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {         // 글 작성 버튼
            @Override
            public void onClick(View view) {
                String postName = InputPostName.getText().toString();
                String postContent = InputPostContent.getText().toString();
//                String text = spinner.getSelectedItem().toString();


//                check(postName, postContent);

                sendPost();               // 글 업로드 통신부분

            }
        });
    }

    public void check(String postName, String postContent) {
        if (postName.equals("") || postContent.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("ERR");
            builder.setMessage("공백인 항목이 있습니다. 공백은 입력할 수 없습니다.");
            builder.setCancelable(false);

            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            err = true;
                        }
                    });
            builder.show();
        } else {
            err = false;
        }
    }

    public void sendPost() {
        String url = "http://10.0.2.2/add_post"; //"http://ccit2020.cafe24.com:8082/add_post";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG",(response.equals("1"));
                        if (response.equals("1")) {
                            Toast.makeText(getApplicationContext(), "글쓰기 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "글쓰기 실패", Toast.LENGTH_SHORT).show();
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

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                final EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
                final EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);  // 글 내용 입력창
//                final Spinner SelectCate = (Spinner) findViewById(R.id.spinner_cate);   // 카테고리 선택
                String name = InputPostName.getText().toString();
                String content = InputPostContent.getText().toString();
//                String select = SelectCate.getSelectedItem().toString();
//                params.put("Kategorie", select);
                params.put("Text", content);
                params.put("Title", name);
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
        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }
}