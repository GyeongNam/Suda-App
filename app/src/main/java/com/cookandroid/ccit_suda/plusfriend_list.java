package com.cookandroid.ccit_suda;

import android.widget.Button;

public class plusfriend_list {
    public String getRoom() {
        return room_idx;
    }

    public void setRoom(String room_idx) {
        this.room_idx = room_idx;
    }

    private String room_idx;
    private String name ="";
    private String follow;
//    private String Name ="";
    public String getFollow() { return follow; }

    public void setFollow(String follow) { this.follow = follow; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

//    public plusfriend_list(String name) {
//        this.name = name;
//    }
}
