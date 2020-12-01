package com.cookandroid.ccit_suda.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TalkDao {
    @Query("SELECT * FROM talk_Contents")
    List<Talk> getAll();

    @Insert
    void insert(Talk talk);

    @Query("SELECT * FROM user_list")
    List<User_list> getAll_user_list();

    @Query("DELETE FROM user_list")
    void deleteAll_user_list();

    @Query("DELETE FROM user_list WHERE user_name = :id")
    void delete_user_list(String id);
    @Insert
    void insert_user_list(User_list user_list);

    @Query("SELECT EXISTS(SELECT * FROM user_list WHERE user_name = :id)")
    boolean isRowIsExist_user_list( String id);



}
