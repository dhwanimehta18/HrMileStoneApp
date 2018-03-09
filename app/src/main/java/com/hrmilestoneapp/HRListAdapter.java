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

public class HRListAdapter extends BaseAdapter {

    private final FragmentActivity activity;

    public HRListAdapter(FragmentActivity activity){
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
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.hrlist_adapter,null);

        ImageView img_hrlist = ll.findViewById(R.id.img_hrlist);
        TextView tv_hrlist = ll.findViewById(R.id.tv_hrlist);

        return ll;
    }
}