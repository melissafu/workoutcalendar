package io.github.melissafmt.finalproject;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TimeUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.melissafmt.finalproject.adapter.VideoAdapter;

import static io.github.melissafmt.finalproject.Constants.API_KEY;

/**
 * Created by Melissa on 12/4/2017.
 */

public class SearchActivity extends AppCompatActivity {
    private static final long MAX_RESULTS = 20L;

    private ListView videoListView;
    private List<SearchResult> videos = new ArrayList<>();
    VideoAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        adapter = new VideoAdapter(this, videos);

        videoListView = (ListView) findViewById(R.id.video_list_view);
        videoListView.setAdapter(adapter);

        final String date = this.getIntent().getExtras().getString("date");

        final Context context = this;
        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult videoResult = videos.get(position);
                String videoId = videoResult.getId().getVideoId();

                Intent detailIntent = new Intent(context, VideoDetailActivity.class);
                detailIntent.putExtra("title", videoResult.getSnippet().getTitle());
                detailIntent.putExtra("videoId", videoId);
                detailIntent.putExtra("date", date);

                startActivity(detailIntent);
            }

        });
    }

    public void saveSearchInput(View v){
        EditText inputText = (EditText) findViewById(R.id.searchBar);
        String inputString = inputText.getText().toString();
        new YoutubeSearchAPI().execute(inputString);
    }

    private class YoutubeSearchAPI extends AsyncTask<String, Void, List<SearchResult>> {
        @Override
        protected List<SearchResult> doInBackground(String... params) {
            List<SearchResult> searchResultList = new ArrayList<>();
            try {
                YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                    }
                }).setApplicationName("Youtube-Search").build();

                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setKey(API_KEY);
                search.setQ(params[0]);
                search.setType("video");
                search.setMaxResults(MAX_RESULTS);

                // Call the API and print results.
                SearchListResponse searchResponse = search.execute();
                searchResultList = searchResponse.getItems();
            } catch (Exception e) {
               e.printStackTrace();
            }
            return searchResultList;
        }

        @Override
        protected void onPostExecute(List<SearchResult> results) {
            videos = results;
            adapter.setmDataSource(results);
            adapter.notifyDataSetChanged();
            super.onPostExecute(results);
        }
    }

}
