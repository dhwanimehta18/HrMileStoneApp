package com.hrmilestoneapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by admin0 on 22-Feb-18.
 */

public class EventListAdapter extends BaseAdapter {

    private final FragmentActivity activity;

    int[] event_pic;
    int[] event_date;
    String[] event_name;
    ArrayList<Events> eventsArrayList;


    public EventListAdapter(FragmentActivity activity, ArrayList<Events> eventsArrayList) {
        this.activity = activity;
        this.eventsArrayList = eventsArrayList;
    }

    @Override
    public int getCount() {
        return eventsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {


        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.event_list_adapter, viewGroup, false);
            holder.event_banner = view.findViewById(R.id.event_banner);
            holder.eventdate = view.findViewById(R.id.event_date);
            holder.tv_event_title = view.findViewById(R.id.tv_event_title);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Events events = (Events) getItem(position);
        String imgpath = events.getImg_banner();
        if (imgpath != null && !imgpath.isEmpty()) {
            byte[] decoded = Base64.decode(imgpath, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            holder.event_banner.setImageBitmap(decodedImage);
        }
        holder.eventdate.setText(events.getEvent_date());
        holder.tv_event_title.setText(events.getEvent_name());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(activity, ModifyEvent.class);
                next.putExtra("EVENT_ID", events.getEvent_id());
                next.putExtra("USER_ID", events.getUserId());
                activity.startActivity(next);
                Toast.makeText(activity, "Event : " + events.getEvent_name(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    class ViewHolder {
        ImageView event_banner;
        TextView eventdate;
        TextView tv_event_title;
    }
}