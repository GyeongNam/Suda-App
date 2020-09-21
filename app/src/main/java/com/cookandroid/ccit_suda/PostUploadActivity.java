package com.cookandroid.ccit_suda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class PostUploadActivity extends AppCompatActivity {

    boolean err = false;
    boolean chk = true;
//    Spinner spinner;
//    private ArrayAdapter arrayAdapter;

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);


        imageview = (ImageView)findViewById(R.id.imgView);
        imageview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        Button back = (Button) findViewById(R.id.bt_backlist);   // 돌아가기 버튼
        Button upload = (Button) findViewById(R.id.bt_upload);    // 글 작성 버튼

        final EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
//        spinner = (Spinner) findViewById(R.id.spinner_cate);  // 글 카테고리 입력창
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
////                R.array.spinner_array, android.R.layout.simple_spinner_item);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        spinner.setAdapter(adapter);
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
                sendPost();

//                check(postName, postContent);
//                if(chk){
//                    Toast.makeText(getApplicationContext(), "글 작성을 다시 해주세요", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    if(!err){
//                                  // 글 업로드 통신부분
//                    }
//                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri selectedImageUri = data.getData();
            imageview.setImageURI(selectedImageUri);
        }
        sendPost();

    }

    public void check(String postName, String postContent) {
        if(postName.equals("") || postContent.equals("")){
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
        }
        else {
            err = false;
        }
    }
    public void sendPost() {
        String url = "http://10.0.2.2/add_post";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
//                        Log.v("TAG",response.equals("1"));
                        if(response.equals("1")){
                            Toast.makeText(getApplicationContext(), "글쓰기 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                            startActivity(intent);
                        }
                        else{
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
                final ImageView InputImageView = (ImageView) findViewById(R.id.imgView);  //이미지 등록


                String name = InputPostName.getText().toString();
                String content = InputPostContent.getText().toString();
//              String select = SelectCate.getSelectedItem().toString();


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