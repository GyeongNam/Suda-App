package com.cookandroid.ccit_suda.ViewModel_user_list;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.paging.AsyncPagedListDiffer;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.cookandroid.ccit_suda.room.Room_list;
import com.cookandroid.ccit_suda.room.Talk;
import com.cookandroid.ccit_suda.room.TalkAndRoom_list;
import com.cookandroid.ccit_suda.room.TalkDao;
import com.cookandroid.ccit_suda.room.User_list;

import java.util.List;

public class User_listViewModel extends AndroidViewModel {

    private User_listRepository user_listRepository;
    private LiveData<List<User_list>> user_list;
    public MediatorLiveData<PagedList<Talk>> mediatorLiveData;
    public LiveData<PagedList<Talk>> liveData;

//    public MediatorLiveData<PagedList<Talk>> init(int room) {
//        PagedList.Config pagedListConfig =
//                (new PagedList.Config.Builder())
//                        .setPrefetchDistance(20)
//                        .setPageSize(30)
//                        .setInitialLoadSizeHint(30)
//                        .setEnablePlaceholders(false)
//                        .build();
//        liveData = new LivePagedListBuilder(user_listRepository.getAll_talk(room), pagedListConfig).build();
//
//        mediatorLiveData.addSource(liveData, new Observer<PagedList<Talk>>() {
//            @Override
//            public void onChanged(@Nullable PagedList<Talk> recipeListPojos) {
//                mediatorLiveData.setValue(recipeListPojos);
//            }
//        });
//        return mediatorLiveData;
//    }
    public LiveData<List<Talk>> get_Talk_listViewModel(int room) {
//        PagedList.Config pagedListConfig =
//                (new PagedList.Config.Builder())
//                        .setPrefetchDistance(20)
//                        .setPageSize(30)
//                        .setInitialLoadSizeHint(30)
//                        .setEnablePlaceholders(false)
//                        .build();
//        LiveData<PagedList<Talk>> data = new LivePagedListBuilder<>(
//                user_listRepository.getAll_talk(room), pagedListConfig
//        ).build();
////        mutableLiveData.postValue();

//        Transformations.switchMap(mutableLiveData, (address) -> {
//            return user_listRepository.getAll_talk(address);
//        });
////
        return user_listRepository.getAll_talk(room);
    }
//    void setUserId(String userId) {
//        this.mutableLiveData.setValue(userId);
//    }

    //    public LiveData<List<Talk>> get_Talk_listViewModel(int room) {
//
//        return user_listRepository.getAll_talk(room);
//    }
//    public LiveData<List<Talk>> get_Talk_listViewModel(int room) {
//
//        return user_listRepository.getAll_talk(room);
//    }
    public LiveData<List<User_list>> get_User_listViewModel() {
        return user_list;
    }

    public LiveData<List<TalkAndRoom_list>> get_Romm_listViewModel(String userinfo) {

        return user_listRepository.getRoom_list(userinfo);
    }

    public LiveData<List<Room_list>> get_Room_friend_list(String username) {
        return user_listRepository.getRoom_friend_list(username);
    }


    public User_listViewModel(@NonNull Application application) {
        super(application);
        user_listRepository = new User_listRepository(application);
        user_list = user_listRepository.getUser_list();
        mediatorLiveData = new MediatorLiveData<>();

    }


}
