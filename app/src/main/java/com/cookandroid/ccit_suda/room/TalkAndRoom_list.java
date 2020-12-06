package com.cookandroid.ccit_suda.room;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TalkAndRoom_list {

    @Embedded
    public Room_list room_list;
    @Relation(parentColumn = "room_number", entityColumn ="chat_room")
    public Talk talk;

}
