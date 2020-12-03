package com.cookandroid.ccit_suda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.cookandroid.ccit_suda.ViewModel_user_list.User_listViewModel;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.List;

public class InviteFriends extends AppCompatActivity {
    RecyclerView listView;
    TalkDatabase talkDatabse;
    User_listViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        PlusroomAdapter plusroomAdapter = new PlusroomAdapter(getApplicationContext());
        listView = findViewById(R.id.invite_list);

        talkDatabse = TalkDatabase.getDatabase(getApplicationContext());
        Log.v("dd",talkDatabse.talkDao().getAll_user_list().toString());
        viewModel = new ViewModelProvider(this).get(User_listViewModel.class);
        viewModel.get_User_listViewModel().observe(this, new Observer<List<User_list>>(){
            public void onChanged(List<User_list> user_lists ) {
                plusroomAdapter.setData(user_lists);
            }
        });

        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setAdapter(plusroomAdapter);
    }
}