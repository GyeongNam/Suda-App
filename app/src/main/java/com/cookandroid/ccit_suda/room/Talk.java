package com.cookandroid.ccit_suda.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "talk_contents")
public class Talk implements Serializable {
    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChatlist() {
        return chatlist;
    }

    public void setChatlist(String chatlist) {
        this.chatlist = chatlist;
    }

    public int getChat_room() {
        return chat_room;
    }

    public void setChat_room(int chat_room) {
        this.chat_room = chat_room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getChat_idx() {
        return chat_idx;
    }

    public void setChat_idx(String chat_idx) {
        this.chat_idx = chat_idx;
    }
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idx") private Integer idx;
    @ColumnInfo(name = "user") private String user;
    @ColumnInfo(name = "chatlist")private String chatlist;
    @ColumnInfo(name = "chat_room")private int chat_room;
    @ColumnInfo(name = "date") private String date;
    @ColumnInfo(name = "chat_idx") private String chat_idx;

    public String getImage_status() {
        return image_status;
    }

    public void setImage_status(String image_status) {
        this.image_status = image_status;
    }

    @ColumnInfo(name = "image_status") private String image_status;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @ColumnInfo(name = "count") private String count;

    public String getChat_read() {
        return chat_read;
    }

    public void setChat_read(String chat_read) {
        this.chat_read = chat_read;
    }

    @ColumnInfo(name = "chat_read") private String chat_read;

    public Talk(Integer idx, String user, String chatlist, int chat_room, String date,String chat_idx,String chat_read,String count,String image_status) {
        this.idx = idx;
        this.user = user;
        this.chatlist = chatlist;
        this.chat_room = chat_room;
        this.date = date;
        this.chat_idx = chat_idx;
        this.chat_read = chat_read;
        this.count = count;
        this.image_status = image_status;
    }
}
