package com.cookandroid.ccit_suda.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_list")
public class User_list implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer F_idx;
    @ColumnInfo( name =  "user_name")
    private String user_name;

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    @ColumnInfo( name =  "room")
    private String room;

    public String getRoom_name() {
        return room_name;
    }

    @ColumnInfo( name =  "room_name")
    private String room_name;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }


    public User_list(Integer F_idx, String user_name,String room, String room_name) {
        F_idx = F_idx;
        this.user_name = user_name;
        this.room = room;
        this.room_name = room_name;
    }

    public Integer getF_idx() {
        return F_idx;
    }

    public void setF_idx(Integer F_idx) {
        F_idx = F_idx;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}
