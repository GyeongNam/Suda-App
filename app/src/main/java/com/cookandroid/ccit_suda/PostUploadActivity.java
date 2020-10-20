package com.cookandroid.ccit_suda;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
//import com.android.volley.toolbox.StringRequest;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class PostUploadActivity extends AppCompatActivity {
    log a = new log();
    Date date = new Date();
    boolean err = false;
    boolean chk = true;
    Spinner spinner;
    List<String> spinnerArray;
    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageView;
    private ImageButton imgDel;
    String imgPath;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        final EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
        final EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);   // 글 내용 입력창
         spinner =  findViewById(R.id.spinner_cate);
         spinnerArray =  new ArrayList<>();



        //갖고올 카테고리 함수구현
        get_categorie();
//        spinnerArray.add("itme1");
        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);





//        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("TAG",String.valueOf(spinner.getSelectedItemPosition()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        imageView = (ImageView)findViewById(R.id.imgView);  //이미지 등록 버튼
        imgDel = (ImageButton)findViewById(R.id.imgbtn2);

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
        ImageButton imgdel = (ImageButton) findViewById(R.id.imgbtn2); //이미지 삭제 버튼

        imgdel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
                imgPath = (null);
            }
        });
        Button back = (Button) findViewById(R.id.bt_backlist);   // 돌아가기 버튼
        Button upload = (Button) findViewById(R.id.bt_upload);    // 글 작성 버튼

        back.setOnClickListener(new View.OnClickListener() {         // 뒤로가기
            @Override
            public void onClick(View view) {
                a.appendLog((date+ "/M/boardActivity/0"));
                Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {         // 글 작성 버튼
            @Override
            public void onClick(View view) {                // 글 작성
                String postName = InputPostName.getText().toString();
                String postContent = InputPostContent.getText().toString();

                check(postName, postContent);
                if (!(postName.isEmpty() || postContent.isEmpty())){
                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                    sendPost();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "공백이 있는지 확인해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });



        //외부 저장소에 권한 필요, 동적 퍼미션
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            int permissionResult= checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(permissionResult== PackageManager.PERMISSION_DENIED){
                String[] permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions,10);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 10:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 사용 가능", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "외부 메모리 읽기/쓰기 제한", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    // 이미지 실험
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

            if(resultCode==RESULT_OK){
                Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();
                Uri uri= data.getData();
                if(uri!=null){
                    imageView.setImageURI(uri);
                    //갤러리앱에서 관리하는 DB정보가 있는데, 그것이 나온다 [실제 파일 경로가 아님!!]
                    //얻어온 Uri는 Gallery앱의 DB번호임. (content://-----/2854)
                    //업로드를 하려면 이미지의 절대경로(실제 경로: file:// -------/aaa.png 이런식)가 필요함
                    //Uri -->절대경로(String)로 변환
                    imgPath= getRealPathFromUri(uri);   //임의로 만든 메소드 (절대경로를 가져오는 메소드)

                    //이미지 경로 uri 확인해보기
                    new AlertDialog.Builder(this).setMessage(uri.toString()+"\n"+imgPath).create().show();
                }
            }else{
                Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
            }


}
    String getRealPathFromUri(Uri uri){
        String[] proj= {MediaStore.Images.Media.DATA};
        CursorLoader loader= new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor= loader.loadInBackground();
        int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result= cursor.getString(column_index);
        cursor.close();
        return  result;
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
//이미지 실험
    public void sendPost() {
                        EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
                EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);  // 글 내용 입력창
                ImageView InputImageView = (ImageView) findViewById(R.id.imgView);  //이미지 등록
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
                String userinfo = sharedPreferences.getString("userinfo","");
                String name = InputPostName.getText().toString();
                String content = InputPostContent.getText().toString();
                int categorie = spinner.getSelectedItemPosition();

        String url = "http://ccit2020.cafe24.com:8082/add_post"; //"http://ccit2020.cafe24.com:8082/add_post"; //http://ccit2020.cafe24.com:8082/login
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("TAG",response);
                a.appendLog(date+"/"+"U"+"/PostUploadActivity/post_add");
                new AlertDialog.Builder(PostUploadActivity.this).setMessage("응답:"+imgPath).create().show();
                a.appendLog(date+"/"+"M"+"/boardActivity/0");
                Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostUploadActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        //요청 객체에 보낼 데이터를 추가
        smpr.addStringParam("Text", content);
        smpr.addStringParam("Title", name);
        smpr.addStringParam("categorie", String.valueOf(categorie));
        smpr.addStringParam("writer", userinfo);
        //이미지 파일 추가
//
        smpr.addFile("image", imgPath);


        smpr.setShouldCache(false);
        AppHelper.requestQueue.add(smpr);
        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }

    public void get_categorie() {
        String url = "http://ccit2020.cafe24.com:8082/get_categorie"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("카테고리",response);

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i =0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                spinnerArray.add(jsonObject.getString("categorie"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        a.appendLog(date+"/"+"E"+"/PostUploadActivity/" +error.toString());
                        Toast.makeText(getApplicationContext(), "서버와 통신이 원할하지 않습니다. 네트워크 연결상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                SharedPreferences sharedPreferences = getSharedPreferences("File", 0);
////                String userinfo = sharedPreferences.getString("userinfo", "");
////                params.put("userid", userinfo);
//
//
//
//                return params;
//            }

//            public Map<String, String> getHeader() throws AuthFailureError{
//                Map<String, String> params = new HashMap<String, String >();
//                params.put("Content-Type", "application/x-www-form-urlencoded");
//                return params;
//            }
        };
        request.setShouldCache(false);

//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
        //Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }
        
}