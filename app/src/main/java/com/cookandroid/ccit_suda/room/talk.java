package com.cookandroid.ccit_suda.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "talk_Contents")
public class talk implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int idx;
    private String user;
    private String chatlist;
    private int chat_room;
    private Date date;

    public talk(int idx, String user, String chatlist, int chat_room, Date date) {
        this.idx = idx;
        this.user = user;
        this.chatlist = chatlist;
        this.chat_room = chat_room;
        this.date = date;
    }
}
