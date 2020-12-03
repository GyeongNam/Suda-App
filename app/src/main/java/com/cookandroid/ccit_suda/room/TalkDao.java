package com.cookandroid.ccit_suda.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TalkDao {
    @Query("SELECT * FROM talk_Contents")
    LiveData<List<Talk>> getAll();

    @Insert
    void insert(Talk talk);

    @Query("SELECT * FROM talk_Contents WHERE chat_room = :room")
    LiveData<List<Talk>> getAll_Talk(int room);

    @Query("SELECT * FROM user_list")
    LiveData<List<User_list>> getAll_user_list();

    @Query("DELETE FROM user_list")
    void deleteAll_user_list();

    @Query("DELETE FROM user_list WHERE user_name = :id")
    void delete_user_list(String id);
    @Insert
    void insert_user_list(User_list user_list);

    @Query("SELECT EXISTS(SELECT * FROM user_list WHERE user_name = :id)")
    boolean isRowIsExist_user_list( String id);

    @Query("SELECT * FROM user_list AS a JOIN talk_Contents AS b ON a.room = b.chat_room GROUP BY b.chat_room ")
    LiveData<List<TalkAndUser_list>> friendroom_user_list();
}
