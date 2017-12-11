package io.github.melissafmt.finalproject.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Melissa on 12/5/2017.
 */

@Database(entities = {CalendarDay.class}, version = 1)
public abstract class CalendarDatabase extends RoomDatabase {
    public abstract CalendarDAO calendarDAO();
}
