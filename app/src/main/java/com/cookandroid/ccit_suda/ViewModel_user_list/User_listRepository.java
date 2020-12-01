package com.cookandroid.ccit_suda.ViewModel_user_list;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.cookandroid.ccit_suda.room.TalkDao;
import com.cookandroid.ccit_suda.room.TalkDatabase;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.List;

public class User_listRepository {
    private  TalkDatabase talkDatabase;
    private  TalkDao talkDao;
    public LiveData<List<User_list>> getUser_list(){

        return talkDao.getAll_user_list();
    }
    public User_listRepository(Application application) {
        talkDatabase = TalkDatabase.getDatabase(application);
        talkDao =talkDatabase.talkDao();
    }
}
