package com.cookandroid.ccit_suda.room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TalkAndUser_list {
    @Embedded
    public User_list user_list;
    @Relation(parentColumn = "room", entityColumn ="chat_room")
    public Talk talk;

}
