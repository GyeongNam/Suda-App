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

    @Query("SELECT * FROM talk_contents WHERE chat_room = :room ORDER BY chat_idx")
    LiveData<List<Talk>> getAll_Talk(int room);

    @Query("SELECT * FROM user_list")
    LiveData<List<User_list>> getAll_user_list();

    @Query("DELETE FROM user_list")
    void deleteAll_user_list();

    @Query("DELETE FROM room_list WHERE room_number = :room")
    void delete_room__list(String room);

    @Query("DELETE FROM room_list WHERE room_number = :room AND user_name = :user AND room_name != 'null'")
    void delete_room__lists(String room, String user);

    @Query("DELETE FROM talk_contents WHERE chat_room = :room ")
    void delete_talk_list(String room);

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
            "GROUP BY chat_room ORDER BY chat_idx DESC) as b " +
            "JOIN room_list as a  ON b.chat_room = a.room_number " +
            "WHERE a.user_name != :userinfo  GROUP BY a.room_number ORDER BY b.chat_idx DESC")
    LiveData<List<TalkAndRoom_list>> friendroom_user_list(String userinfo);

    //Room_list insert
    @Insert
    void insert_room_list(Room_list room_list);


    @Query("SELECT user_name FROM room_list WHERE room_number = :username")
    LiveData<List<Room_list>> getAll_chat_user_list(String username);

    @Query("SELECT room_number FROM room_list WHERE user_name = :id")
    List<Room_list> get_room_number(String id);

    @Query("SELECT chat_room, chat_idx FROM talk_contents GROUP BY chat_room ORDER BY chat_idx DESC")
    List<Talk> get_lately_chat_list();

    @Query("SELECT EXISTS(SELECT * FROM talk_contents WHERE chat_idx = :idx)")
    boolean isRowIsExist_talk_list(String idx);

    @Query("SELECT COUNT(*) FROM talk_contents WHERE chat_read  = 0 AND chat_room = :room")
    String not_read_chat_count(String room);

    @Query("UPDATE talk_contents SET chat_read = 1 WHERE chat_read = 0 AND chat_room = :room")
    void update_read(int room);

    @Query("SELECT chat_idx FROM talk_contents WHERE chat_room = :room ORDER BY idx DESC LIMIT 1")
    int get_lately_chat_idx(int room);

    @Query("SELECT * FROM talk_contents WHERE chat_room = :room LIMIT 1")
    List<Talk> get_first_chat_idx(int room);

    @Query("UPDATE talk_contents SET count = :count WHERE chat_idx = :chat_idx")
    void update_chat_read_server(String count,String chat_idx );

    @Query("SELECT COUNT(*) FROM room_list WHERE room_number = :room")
    LiveData<Integer> room_user_list_count(int room);

    @Query("UPDATE room_list SET lately_chat_idx = :lately_chat_idx WHERE user_name = :user AND room_number = :room_number")
    void update_lately_chat_idx(String user, String lately_chat_idx, String room_number);

    @Query("UPDATE talk_contents SET count = count - 1 WHERE chat_idx > (SELECT lately_chat_idx FROM room_list WHERE user_name = :user AND room_number = :room_number ) AND chat_room = :room_number")
    void update_talk_contents_count(String user, String room_number);

    @Query("SELECT EXISTS(SELECT * FROM room_list WHERE user_name = :id AND room_number = :room AND lately_chat_idx = :lately_chat_idx)")
    boolean isRowIsExist_update_status(String id, String room,String lately_chat_idx);
}
