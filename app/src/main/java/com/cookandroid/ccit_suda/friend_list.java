package com.cookandroid.ccit_suda;


import android.widget.Button;
import android.widget.ImageView;

public class friend_list {
    private String name;
    private String follow;
    private String room_idx;

    public String getRoom() {
        return room_idx;
    }

    public void setRoom(String room_idx) {
        this.room_idx = room_idx;
    }

    public String getFollow() { return follow; }

    public void setFollow(String follow) { this.follow = follow; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
