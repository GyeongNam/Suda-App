package com.cookandroid.ccit_suda.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {talk.class}, version = 1)
public abstract class talkDatabase extends RoomDatabase {

    public abstract talkDao talkDao();

    private  static talkDatabase INSTANCE;

    public static talkDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (talkDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            talkDatabase.class, "talk_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
