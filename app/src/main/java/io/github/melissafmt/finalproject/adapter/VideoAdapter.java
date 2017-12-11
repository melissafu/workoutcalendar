package io.github.melissafmt.finalproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

import io.github.melissafmt.finalproject.R;

/**
 * Created by Melissa on 12/5/2017.
 */

public class VideoAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<SearchResult> mDataSource;

    public VideoAdapter(Context context, List<SearchResult> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_item_video, parent, false);

        TextView titleTextView = (TextView) rowView.findViewById(R.id.video_list_title);

        TextView channelTextView = (TextView) rowView.findViewById(R.id.video_list_channel);

        SearchResult videoResult = (SearchResult) getItem(position);

        titleTextView.setText(videoResult.getSnippet().getTitle());
        channelTextView.setText(videoResult.getSnippet().getChannelTitle());

        return rowView;
    }

    public List<SearchResult> getmDataSource() {
        return mDataSource;
    }

    public void setmDataSource(List<SearchResult> mDataSource) {
        this.mDataSource = mDataSource;
    }
}
