package com.cookandroid.ccit_suda;

public class Commentlist {
    String Comment,Writer,Date,Num,Parent,Recomment;
    private int type ;
    public void setType(int type) {
        this.type = type ;
    }




    public void setParent(String parent) {
        Parent = parent;
    }

    public String getParent() {
        return Parent;
    }



    public void setComment(String comment) {
        Comment = comment;
    }

    public void setWriter(String writer) {
        Writer = writer;
    }

    public void setDate(String date) {
        Date = date;
    }


    public int getType() {
        return this.type ;
    }
    public String getComment() {
        return Comment;
    }

    public String getWriter() {
        return Writer;
    }

    public String getDate() {
        return Date;
    }

    public void setNum(String setnum) {
        Num = setnum;
    }

    public String getNum() {
        return Num;
    }
}
