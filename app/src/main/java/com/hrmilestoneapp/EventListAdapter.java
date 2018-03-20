package com.hrmilestoneapp;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin0 on 22-Feb-18.
 */

public class EventListAdapter extends BaseAdapter {

    private final FragmentActivity activity;

    int[] event_pic;
    int[] event_date;
    String[] event_name;


    public EventListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return event_pic.length;
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
        CoordinatorLayout ll = (CoordinatorLayout) inflater.inflate(R.layout.event_list_adapter, null);

        ImageView event_banner = ll.findViewById(R.id.event_banner);
        ImageView eventdate = ll.findViewById(R.id.event_date);
        TextView tv_event_title = ll.findViewById(R.id.tv_event_title);

        event_banner.setImageResource(event_pic[position]);
        eventdate.setImageResource(event_date[position]);
        tv_event_title.setText(event_name[position]);

        return ll;
    }

    public void eventPic(int[] event_pic) {
        this.event_pic = event_pic;
    }

    public void eventDate(int[] event_date) {
        this.event_date = event_date;
    }

    public void eventName(String[] event_name) {
        this.event_name = event_name;
    }
}