package io.github.melissafmt.finalproject.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Melissa on 12/5/2017.
 */

@Dao
public interface CalendarDAO {
    @Insert
    void insert(CalendarDay calendarDay);

    @Update
    void update(CalendarDay calendarDay);

    @Query("SELECT * FROM CalendarDay WHERE date =:date")
    CalendarDay findOne(String date);

    @Query("SELECT * FROM CalendarDay")
    List<CalendarDay> findAll();
}
