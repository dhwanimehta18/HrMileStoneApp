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

public class HRListAdapter extends BaseAdapter {

    private final FragmentActivity activity;
    private final ArrayList<User> userArrayList;


    public HRListAdapter(FragmentActivity activity, ArrayList<User> userArrayList) {
        this.activity = activity;
        this.userArrayList = userArrayList;
    }


    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return userArrayList.get(i);
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
            view = LayoutInflater.from(activity).inflate(R.layout.hrlist_adapter, viewGroup, false);
            holder.img_hrlist = view.findViewById(R.id.img_hrlist);
            holder.tv_hrlist = view.findViewById(R.id.tv_hrlist);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        final User user = (User) getItem(position);
        String imgpath = user.getPath();
        if (imgpath != null && !imgpath.isEmpty()) {
            byte[] decoded = Base64.decode(imgpath, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            holder.img_hrlist.setImageBitmap(decodedImage);
        }
        holder.tv_hrlist.setText(user.getUser_email());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "" + user.getUser_email(), Toast.LENGTH_LONG).show();
                Intent next = new Intent(activity, ChatActivity.class);
                next.putExtra("userId", user.getUserKey());
                next.putExtra("userEmail", user.getUser_email());
                activity.startActivity(next);

            }
        });

        return view;
    }

    class ViewHolder {
        TextView tv_hrlist;
        ImageView img_hrlist;
    }
}