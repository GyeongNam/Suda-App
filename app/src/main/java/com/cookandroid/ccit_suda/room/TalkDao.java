package com.cookandroid.ccit_suda.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface TalkDao {
    @Query("SELECT * FROM talk_contents")
    LiveData<List<Talk>> getAll();

    @Insert
    void insert(Talk talk);

    @Query("SELECT * FROM talk_contents WHERE chat_room = :room")
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
    boolean isRowIsExist_user_list(String id);

    @Query("SELECT EXISTS(SELECT * FROM room_list WHERE user_name = :id AND room_number = :room)")
    boolean isRowIsExist_user_room_list(String id,String room);

    @Query("SELECT * FROM " +
            "(SELECT * FROM talk_contents " +
            "GROUP BY chat_room ORDER BY idx DESC) as b " +
            "JOIN room_list as a  ON b.chat_room = a.room_number " +
            "WHERE a.user_name != :userinfo  GROUP BY a.room_number ORDER BY b.idx DESC")
    LiveData<List<TalkAndRoom_list>> friendroom_user_list(String userinfo);

    //Room_list insert
    @Insert
    void insert_room_list(Room_list room_list);

    @Query("SELECT room_number FROM room_list WHERE user_name = :id")
    List<Room_list> get_room_number(String id);

    @Query("SELECT chat_room, chat_idx FROM talk_contents GROUP BY chat_room ORDER BY chat_idx DESC")
    List<Talk> get_lately_chat_list();
    @Query("SELECT EXISTS(SELECT * FROM talk_contents WHERE chat_idx = :idx)")
    boolean isRowIsExist_talk_list(String idx);
}
