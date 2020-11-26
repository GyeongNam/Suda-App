package com.cookandroid.ccit_suda;

public class chatroom_list {
    private String chatroom_user_name;
    private String Last_talk;
    private String Last_talk_time;

    public String getChatroom_user_name() { return chatroom_user_name; }

    public void setChatroom_user_name(String chatroom_user_name) { this.chatroom_user_name = chatroom_user_name; }

    public String getLast_talk() { return Last_talk; }

    public void setLast_talk(String last_talk) { Last_talk = last_talk; }

    public String getLast_talk_time() { return Last_talk_time; }

    public void setLast_talk_time(String last_talk_time) { Last_talk_time = last_talk_time; }

    public chatroom_list(String chatroom_user_name, String last_talk, String last_talk_time) {
        this.chatroom_user_name = chatroom_user_name;
        Last_talk = last_talk;
        Last_talk_time = last_talk_time;
    }
}
