package com.hrmilestoneapp;

import android.app.Dialog;
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

import com.google.firebase.database.DatabaseReference;
import com.hrmilestoneapp.utils.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by admin0 on 23-Feb-18.
 */

public class PhotosAdapter extends BaseAdapter {

    private String USER_KEY;
    DatabaseReference fref;

    private final FragmentActivity activity;
    private int imgData[];
    String[] event_name;
    ArrayList<Photos> photosArrayList;

    public PhotosAdapter(FragmentActivity activity, ArrayList<Photos> photosArrayList) {
        this.activity = activity;
        this.photosArrayList = photosArrayList;
    }

    public void updateResults(ArrayList<Photos> arrayList) {
        photosArrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return photosArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return photosArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        USER_KEY = PreferenceManager.getprefUserKey(activity);

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(activity).inflate(R.layout.photos_adapter, viewGroup, false);
            holder.img_photo_list_adapter = view.findViewById(R.id.img_photo_list_adapter);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Photos photos = (Photos) getItem(position);
        String imgpath = photos.getPhotoPath();
        if (imgpath != null && !imgpath.isEmpty()) {
            byte[] decoded = Base64.decode(imgpath, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            holder.img_photo_list_adapter.setImageBitmap(decodedImage);
        }

        holder.img_photo_list_adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.photos_dialog);
                TextView tv_photo_upload_time = dialog.findViewById(R.id.tv_photo_upload_time);
                TextView tv_photos_upload_name = dialog.findViewById(R.id.tv_photos_upload_name);
                tv_photo_upload_time.setText(photos.getTime() + " " + photos.getDate());
                tv_photos_upload_name.setText(photos.getUserFName() + " " + photos.getUserLName());

                dialog.show();
                //Toast.makeText(activity, "" + photos.getUserId(), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    class ViewHolder {
        public ImageView img_photo_list_adapter;
    }
    /*public void imageId(int[] imgData) {
        this.imgData = imgData;
    }*/
}
