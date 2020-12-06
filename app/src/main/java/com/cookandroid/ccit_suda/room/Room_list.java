package com.cookandroid.ccit_suda.room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "room_list")
public class Room_list implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer R_idx;

    @ColumnInfo(name = "user_name")
    private String user_name;

    @ColumnInfo(name = "room_number")
    private String room_number;


    @ColumnInfo(name = "room_name")
    private String room_name;


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }



    public Integer getR_idx() {
        return R_idx;
    }

    public void setR_idx(Integer R_idx) {
        R_idx = R_idx;
    }

    public String getUser() {
        return user_name;
    }

    public void setUser(String user_name) {
        this.user_name = user_name;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }
    //생성자
    public Room_list(Integer R_idx, String user_name, String room_number, String room_name) {
        R_idx = R_idx;
        this.user_name = user_name;
        this.room_number = room_number;
        this.room_name = room_name;
    }


}
