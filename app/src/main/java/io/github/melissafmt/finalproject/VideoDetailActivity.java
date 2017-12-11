/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.melissafmt.finalproject;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.api.services.youtube.model.SearchResult;

import io.github.melissafmt.finalproject.persistence.CalendarDatabase;
import io.github.melissafmt.finalproject.persistence.CalendarDay;

import static io.github.melissafmt.finalproject.Constants.API_KEY;
import static io.github.melissafmt.finalproject.Constants.CALENDAR_DB;

/**
 * Created by Melissa on 12/5/2017.
 */

public class VideoDetailActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerFragment myYouTubePlayerFragment;
    private String VIDEO_ID;

    private CalendarDatabase calendarDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        String title = this.getIntent().getExtras().getString("title");
        String videoId = this.getIntent().getExtras().getString("videoId");
        this.VIDEO_ID = videoId;
        calendarDatabase = Room.databaseBuilder(getApplicationContext(),
                CalendarDatabase.class, CALENDAR_DB).build();

        setTitle(title);

        myYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
                .findFragmentById(R.id.youtubeplayerfragment);
        myYouTubePlayerFragment.initialize(API_KEY, this);

    }

    public void saveVideoFunction(View v) {
        String date = this.getIntent().getExtras().getString("date");
        new DatabaseAddAsync().execute(date, VIDEO_ID);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        if (!restored) {
            youTubePlayer.cueVideo(VIDEO_ID);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private class DatabaseAddAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... results) {
            String date = results[0];
            String videoId = results[1];

            CalendarDay day = calendarDatabase.calendarDAO().findOne(date);
            if (day == null) {
                day = new CalendarDay();
                day.setVideoId(videoId);
                day.setDate(date);
                calendarDatabase.calendarDAO().insert(day);
            } else {
                day.setVideoId(videoId);
                calendarDatabase.calendarDAO().update(day);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(VideoDetailActivity.this, CalendarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
