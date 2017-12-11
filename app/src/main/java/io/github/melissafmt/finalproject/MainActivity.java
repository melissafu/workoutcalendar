package io.github.melissafmt.finalproject;

import android.app.FragmentTransaction;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class MainActivity extends YouTubeBaseActivity
        implements YouTubePlayer.OnInitializedListener {
    private static final String TAG = "MainActivity";
    private YouTubePlayerFragment myYouTubePlayerFragment;

    private TextView thedate;
    private String viewDate;
    private Button btngocalendar;

    private static String VIDEO_ID = null;

    private CalendarDatabase calendarDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thedate = (TextView) findViewById(R.id.date);
        btngocalendar = (Button) findViewById(R.id.btngocalendar);

        Intent incomingIntent = this.getIntent();
        String date = incomingIntent.getStringExtra("date");
        this.viewDate = date;
        thedate.setText(date);

        calendarDatabase = Room.databaseBuilder(getApplicationContext(),
                CalendarDatabase.class, CALENDAR_DB).build();

        new DatabaseGetAsync().execute();

        btngocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        });

        if (VIDEO_ID != null) {
            myYouTubePlayerFragment = (YouTubePlayerFragment)getFragmentManager()
                    .findFragmentById(R.id.youtubeplayerfragment);
            myYouTubePlayerFragment.initialize(API_KEY, this);
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox2);
            checkBox.setVisibility(View.VISIBLE);
            View frag = findViewById(R.id.youtubeplayerfragment);
            frag.setVisibility(View.VISIBLE);
        }
        else {
            CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox2);
            checkBox.setVisibility(View.GONE);
            View frag = findViewById(R.id.youtubeplayerfragment);
            frag.setVisibility(View.GONE);
        }

    }

    public void searchFunction(View v){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        intent.putExtra("date", viewDate);

        startActivity(intent);
    }

    public void saveNotesFunction(View v){
        EditText noteText = (EditText) findViewById(R.id.editText);
        String note = noteText.getText().toString();
        new DatabaseSaveNoteAsync().execute(viewDate, note);
    }

    public void onSaveCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        new DatabaseSaveCheckBoxAsync().execute(checked);
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

    private class DatabaseGetAsync extends AsyncTask<SearchResult, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(SearchResult... results) {
            CalendarDay day = calendarDatabase.calendarDAO().findOne(viewDate);
            if (day != null) {
                VIDEO_ID = day.getVideoId();
                EditText noteText = (EditText) findViewById(R.id.editText);
                noteText.setText(day.getNote());

                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox2);
                if (day.getSaved() != null && day.getSaved()) {
                    checkBox.setChecked(true);
                }
            }
            else {
                VIDEO_ID = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class DatabaseSaveNoteAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... results) {
            String date = results[0];
            String note = results[1];

            CalendarDay day = calendarDatabase.calendarDAO().findOne(date);
            if (day == null) {
                day = new CalendarDay();
                day.setNote(note);
                day.setDate(date);
                calendarDatabase.calendarDAO().insert(day);
            } else {
                day.setNote(note);
                calendarDatabase.calendarDAO().update(day);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class DatabaseSaveCheckBoxAsync extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Boolean... results) {
            String date = viewDate;
            Boolean isChecked = results[0];

            CalendarDay day = calendarDatabase.calendarDAO().findOne(date);
            if (day == null) {
                day = new CalendarDay();
                day.setSaved(isChecked);
                day.setDate(date);
                calendarDatabase.calendarDAO().insert(day);
            } else {
                day.setSaved(isChecked);
                calendarDatabase.calendarDAO().update(day);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}