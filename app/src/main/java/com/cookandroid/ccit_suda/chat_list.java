package com.cookandroid.ccit_suda;

import java.util.Date;

public class chat_list {

    private String nickname;
    private Date write_date;
    private String content;

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public Date getWrite_date() { return write_date; }

    public void setWrite_date(Date write_date) { this.write_date = write_date; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public chat_list(String nickname, Date write_date, String content) {
        this.nickname = nickname;
        this.write_date = write_date;
        this.content = content;
    }
}
