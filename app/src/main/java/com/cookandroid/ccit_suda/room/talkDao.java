package com.cookandroid.ccit_suda.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface talkDao {
    @Query("SELECT * FROM talk_Contents")
    List<talk> getAll();

    @Insert
    void insert(talk talk);

}
