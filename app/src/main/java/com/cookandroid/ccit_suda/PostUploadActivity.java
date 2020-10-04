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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
//import com.android.volley.toolbox.StringRequest;

//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;

public class PostUploadActivity extends AppCompatActivity {

    boolean err = false;
    boolean chk = true;


    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageView;
    String imgPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        final EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
        final EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);   // 글 내용 입력창
        Spinner spinner =  findViewById(R.id.spinner_cate);

        imageView = (ImageView)findViewById(R.id.imgView);  //이미지 등록 버튼



        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        Button back = (Button) findViewById(R.id.bt_backlist);   // 돌아가기 버튼
        Button upload = (Button) findViewById(R.id.bt_upload);    // 글 작성 버튼

        back.setOnClickListener(new View.OnClickListener() {         // 뒤로가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {         // 글 작성 버튼
            @Override
            public void onClick(View view) {                // 글 작성
                String postName = InputPostName.getText().toString();
                String postContent = InputPostContent.getText().toString();
                Intent intent = new Intent(getApplicationContext(), boardActivity.class);
                startActivity(intent);

                sendPost();
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

//이미지 실험
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
                        EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
                EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);  // 글 내용 입력창
                ImageView InputImageView = (ImageView) findViewById(R.id.imgView);  //이미지 등록
                Spinner spinner =  findViewById(R.id.spinner_cate); // 스피너
        SharedPreferences sharedPreferences = getSharedPreferences("File",0);
                String userinfo = sharedPreferences.getString("userinfo","");
                String name = InputPostName.getText().toString();
                String content = InputPostContent.getText().toString();
                String kategorie = spinner.getSelectedItem().toString();

        String url = "http://10.0.2.2/add_post"; //"http://10.0.2.2/add_post"; //http://ccit2020.cafe24.com:8082/login
        SimpleMultiPartRequest smpr= new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("TAG",response);
                new AlertDialog.Builder(PostUploadActivity.this).setMessage("응답:"+imgPath).create().show();
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
        smpr.addStringParam("kategorie", kategorie);
        smpr.addStringParam("writer", userinfo);
        //이미지 파일 추가
//
        smpr.addFile("image", imgPath);


        smpr.setShouldCache(false);
        AppHelper.requestQueue.add(smpr);
        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }
        
}