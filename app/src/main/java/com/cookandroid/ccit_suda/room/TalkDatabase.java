package com.cookandroid.ccit_suda.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Talk.class,User_list.class}, version = 1)
public abstract class TalkDatabase extends RoomDatabase {

    public abstract TalkDao talkDao();

    private  static TalkDatabase INSTANCE;

    public static TalkDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TalkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TalkDatabase.class, "talk_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
