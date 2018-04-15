package com.hrmilestoneapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DashboardAdapter extends BaseAdapter {

    Context context;
    ArrayList<DashboardModel> dashboardMessageArrayList;
    LayoutInflater inflater;
    ListView listView;
    TextView Comments;
    private TextView tv_status_dashboard, tv_name_dashboard, tv_dashboard_datetime;


    public DashboardAdapter(Context context, ArrayList<DashboardModel> dashboardMessageArrayList) {
        this.context = context;
        this.dashboardMessageArrayList = dashboardMessageArrayList;
    }


    @Override
    public int getCount() {

        return dashboardMessageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dashboardMessageArrayList.get(getCount() - position - 1);

    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)

    {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.row_dashboard, parent, false);
        }

        // get current item to be displayed
        DashboardModel rateAndReview = (DashboardModel) getItem(position);

        // get the TextView for item name and item description
        tv_status_dashboard = (TextView) convertView.findViewById(R.id.tv_status_dashboard);
        tv_name_dashboard = (TextView) convertView.findViewById(R.id.tv_name_dashboard);
        tv_dashboard_datetime = convertView.findViewById(R.id.tv_dashboard_datetime);
//        Rating = (TextView)convertView.findViewById(R.id.rating);

        //sets the text for item name and item description from the current item object
        tv_status_dashboard.setText(rateAndReview.getDashboardMessages());
        tv_name_dashboard.setText("- " + rateAndReview.getSenderName());
        tv_dashboard_datetime.setText(rateAndReview.getTime() + " " + rateAndReview.getDate());
//        Rating.setText(rateAndReview.getRating());


        return convertView;
    }
}
