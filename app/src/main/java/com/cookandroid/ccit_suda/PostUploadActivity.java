package com.cookandroid.ccit_suda;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cookandroid.ccit_suda.VolleyMultipartRequest.DataPart;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostUploadActivity extends AppCompatActivity {

    boolean err = false;
    boolean chk = true;


    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;

    //실험실
    private static final String ROOT_URL = "http://seoforworld.com/api/v1/file-upload.php";
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST =1 ;
    private Bitmap bitmap;
    private String filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        final EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
        final EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);   // 글 내용 입력창
        Spinner spinner =  findViewById(R.id.spinner_cate);

        imageview = (ImageView)findViewById(R.id.imgView);  //이미지 등록 버튼

//        imageview.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent, GET_GALLERY_IMAGE);
//            }
//        });
        //adding click listener to button
        // 이미지 실험
        findViewById(R.id.imgView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(PostUploadActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(PostUploadActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(PostUploadActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_PERMISSIONS);
                    }
                } else {
                    Log.e("Else", "Else");
                    showFileChooser();
                }


            }
        });
        // 이미지 실험

        Button back = (Button) findViewById(R.id.bt_backlist);   // 돌아가기 버튼
        Button upload = (Button) findViewById(R.id.bt_upload);    // 글 작성 버튼

        back.setOnClickListener(new View.OnClickListener() {         // 뒤로가기
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostListActivity.class);
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {         // 글 작성 버튼
            @Override
            public void onClick(View view) {                // 글 작성
                String postName = InputPostName.getText().toString();
                String postContent = InputPostContent.getText().toString();

                sendPost();
            }
        });
    }

    // 이미지 실험
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    // 이미지 실험

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri selectedImageUri = data.getData();
//            imageview.setImageURI(selectedImageUri);
//
//        }
//        else{
//            Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
    // 이미지 실험
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
        Uri picUri = data.getData();
        filePath = getPath(picUri);
        if (filePath != null) {
            try {

                Log.d("filePath", String.valueOf(filePath));
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                uploadBitmap(bitmap);
                imageview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(
                    PostUploadActivity.this,"no image selected",
                    Toast.LENGTH_LONG).show();
        }
    }

}
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                    }
                });
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
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, ROOT_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                });

        String url = "http://ccit2020.cafe24.com:8082/add_post"; //"http://10.0.2.2/add_post"; //http://ccit2020.cafe24.com:8082/login
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        if(response.equals("1")){
                            Toast.makeText(getApplicationContext(), "글쓰기 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), boardActivity.class);
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
                EditText InputPostName = (EditText) findViewById(R.id.et_postname); // 글 제목 입력창
                EditText InputPostContent = (EditText) findViewById(R.id.et_postcontent);  // 글 내용 입력창
                ImageView InputImageView = (ImageView) findViewById(R.id.imgView);  //이미지 등록
                Spinner spinner =  findViewById(R.id.spinner_cate); // 스피너

                SharedPreferences sharedPreferences = getSharedPreferences("File",0);
                String userinfo = sharedPreferences.getString("userinfo","");

                String name = InputPostName.getText().toString();
                String content = InputPostContent.getText().toString();
                String kategorie = spinner.getSelectedItem().toString();

                params.put("kategorie", kategorie);
                params.put("Text", content);
                params.put("Title", name);
                params.put("image", "1234");
                params.put("writer", userinfo);
                return params;
            }
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
        Toast.makeText(getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }
}