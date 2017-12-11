package io.github.melissafmt.finalproject.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Melissa on 12/5/2017.
 */

@Entity
public class CalendarDay {
    @PrimaryKey(autoGenerate = true)
    private int slNo;

    private String date;

    private String videoId;

    private String note;

    private Boolean saved;

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public int getSlNo() {
        return slNo;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarDay that = (CalendarDay) o;

        if (slNo != that.slNo) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (videoId != null ? !videoId.equals(that.videoId) : that.videoId != null) return false;
        return note != null ? note.equals(that.note) : that.note == null;
    }

    @Override
    public int hashCode() {
        int result = slNo;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (videoId != null ? videoId.hashCode() : 0);
        result = 31 * result + (note != null ? note.hashCode() : 0);
        return result;
    }
}
