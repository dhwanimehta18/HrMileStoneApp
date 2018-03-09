package com.hrmilestoneapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by admin0 on 22-Feb-18.
 */

public class EventListAdapter extends BaseAdapter {

    private final FragmentActivity activity;

    public EventListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.event_list_adapter, null);

        ImageView event_banner = ll.findViewById(R.id.event_banner);
        ImageView event_date = ll.findViewById(R.id.event_date);
        TextView tv_event_title = ll.findViewById(R.id.tv_event_title);

        return ll;
    }
}