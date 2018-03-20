package com.hrmilestoneapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by admin0 on 23-Feb-18.
 */

public class PhotosAdapter extends BaseAdapter {

    private final FragmentActivity activity;
    private int imgData[];

    public PhotosAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imgData.length;
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.photos_adapter, null);

        ImageView img_photo_list_adapter = ll.findViewById(R.id.img_photo_list_adapter);
        img_photo_list_adapter.setImageResource(imgData[i]);

        return ll;
    }

    public void imageId(int[] imgData) {
        this.imgData = imgData;
    }
}
