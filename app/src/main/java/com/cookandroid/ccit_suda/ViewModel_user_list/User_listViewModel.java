package com.cookandroid.ccit_suda.ViewModel_user_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkAndRoom_list;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.List;

public class User_listViewModel extends AndroidViewModel{

    private  User_listRepository user_listRepository;
    private  LiveData<List<User_list>> user_list;


    public LiveData<List<Talk>> get_Talk_listViewModel(int room) { return  user_listRepository.getAll_talk(room); };

    public LiveData<List<User_list>> get_User_listViewModel(){ return user_list; }

    public LiveData<List<TalkAndRoom_list>> get_Romm_listViewModel(String userinfo){

        return user_listRepository.getRoom_list(userinfo);
    }
    public LiveData<List<Room_list>> get_Room_friend_list(String username) {
        return user_listRepository.getRoom_friend_list(username);
    }


    public User_listViewModel(@NonNull Application application) {
        super(application);
        user_listRepository = new User_listRepository(application);
        user_list = user_listRepository.getUser_list();


    }

}
