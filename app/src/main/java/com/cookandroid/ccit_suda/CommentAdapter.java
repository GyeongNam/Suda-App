package com.cookandroid.ccit_suda;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentAdapter extends BaseAdapter {
    int position;
    String array;
    TextView rereply, del_reply;
    EditText editText;
    ArrayList<Commentlist> commentlist;
    String Number;
    Context mContext;
    LayoutInflater inflater;
    LinearLayout replylayout;
    private static final int ITEM_VIEW_TYPE_reply = 0;
    private static final int ITEM_VIEW_TYPE_rereply = 1;
    private static final int ITEM_VIEW_TYPE_MAX = 2;

    public CommentAdapter(Context context, ArrayList<Commentlist> items) {
        this.commentlist = new ArrayList<>();
        this.commentlist.addAll(items);
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX;
    }

    // position 위치의 아이템 타입 리턴.
    @Override
    public int getItemViewType(int position) {
        return commentlist.get(position).getType();
    }

    public void setItem(ArrayList<Commentlist> items) {
        if (commentlist != null) {
            commentlist.clear();
            commentlist.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return commentlist.size();
    }

    @Override
    public Object getItem(int position) {
        return commentlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("File", 0);
        String userinfo = sharedPreferences.getString("userinfo", "");
        int viewType = this.getItemViewType(position);
        convertView = inflater.inflate(R.layout.comment_list_item, parent, false);
        holder = new Holder();
        holder.writer = convertView.findViewById(R.id.id);
        holder.comment = convertView.findViewById(R.id.comment);
//            holder.date = convertView.findViewById(R.id.date);
        convertView.setTag(holder);
        if (convertView == null) {


        } else {
            holder = (Holder) convertView.getTag();
        }
        TextView indicate = convertView.findViewById(R.id.indicate);
        LinearLayout ifrereply = convertView.findViewById(R.id.ifrereply);
        LinearLayout no_up = convertView.findViewById(R.id.no_up);

        final Commentlist item = commentlist.get(position);

        if (item != null) {
            if (item.getWriter() == "null") {
                LinearLayout replylayout = convertView.findViewById(R.id.replylayout);
                replylayout.setVisibility(View.GONE);
                return convertView;
            }



            array = item.getNum();
            Log.v("TAG", "배열확인:" + array);

//            holder.writer.setText(item.getWriter());
//            holder.comment.setText(item.getComment());
//            holder.indicate.setVisibility(View.GONE);
            Log.v("TAG", "CONVERTVIEW" + convertView);
//
            if (item.getParent() != "null") {
                //대댓글
                LinearLayout replylayout = convertView.findViewById(R.id.replylayout);
                holder.writer.setText(item.getWriter());
                holder.comment.setText(item.getComment());
                LinearLayout.LayoutParams layparam = (LinearLayout.LayoutParams) replylayout.getLayoutParams();
                layparam.leftMargin = 80;
                indicate.setVisibility(View.VISIBLE);
                ifrereply.setVisibility(View.GONE);
                no_up.setVisibility(View.GONE);
                replylayout.setLayoutParams(layparam);
                Log.v("TAG", "생성시점 알아보기1:" + item.getNum());
            } else {
                //댓글
                Log.v("TAG", "생성시점 알아보기2:" + item.getNum());
                indicate.setVisibility(View.INVISIBLE);
                holder.writer.setText(item.getWriter());
                holder.comment.setText(item.getComment());
            }


        } else {

        }
        rereply = (TextView) convertView.findViewById(R.id.rereply);
        del_reply = (TextView) convertView.findViewById(R.id.del_reply);

        editText = ((PostdetailActivity) mContext).replytext;
        //사용자 여부에따라 삭제 기능 활성/비활성
            if(!(userinfo.equals(item.getWriter()))){
                Log.v("TAG","뭐지??"+userinfo);
                Log.v("TAG","뭐지??"+item.getWriter());
                del_reply.setVisibility(View.GONE);
            }



        rereply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext.getApplicationContext(), "눌럿음" + item.getNum(), Toast.LENGTH_SHORT).show();
                Log.v("TAG", "눌럿음" + item.getNum());
//                Log.v("TAG","해당아이템"+commentlist.remove(position));
                Number = item.getNum();
                //답글 클릭시 edittext 포커스 해주는 코드
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        editText.setFocusableInTouchMode(true);
                        editText.requestFocus();

                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                        imm.showSoftInput(editText, 0);

                    }
                });
                Log.v("TAG", "Number:" + Number);
                Log.v("TAG", "텍스트:" + editText.getText());

            }
        });
        del_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 다이얼로그 바디
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(mContext);
                // 다이얼로그 메세지
                alertdialog.setMessage("댓글을 정말 삭제 하시겠습니까?.");

                // 확인버튼
                alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delreply(item.getNum());
                        commentlist.remove(position);
                        notifyDataSetChanged();
//                        Toast.makeText(mContext, "'확인'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 취소버튼
                alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(mContext, "'취소'버튼을 눌렀습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                // 메인 다이얼로그 생성
                AlertDialog alert = alertdialog.create();
                // 아이콘 설정

                // 타이틀
                alert.setTitle("알림");
                // 다이얼로그 보기
                alert.show();
                Log.v("TAG", "삭제버튼 눌럿음 : " + item.getNum());


            }
        });

        return convertView;

    }

    class Holder {
        public TextView writer, comment, date;


    }

    public void delreply(final String num) {
        String url = "http://10.0.2.2/del_reply"; //"http://ccit2020.cafe24.com:8082/login";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("File", 0);
                        String userinfo = sharedPreferences.getString("userinfo", "");
//                        TextView username = (TextView) ((Activity)mContext).findViewById(R.id.username);
//                        username.setText("환영합니다 " + userinfo + " 님");


//                        Toast.makeText(getApplicationContext(), "응답->" + response, Toast.LENGTH_SHORT).show();
                        Log.v("TAG", response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext.getApplicationContext(), "에러 ->" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.v("TAG", error.toString());
                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SharedPreferences sharedPreferences = mContext.getSharedPreferences("File", 0);
                String userinfo = sharedPreferences.getString("userinfo", "");
                params.put("userid", userinfo);
                params.put("c_num", num);
//                params.put("recomment", 텍스트);

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
        Toast.makeText(mContext.getApplicationContext(), "요청 보냄", Toast.LENGTH_SHORT).show();
    }

}

