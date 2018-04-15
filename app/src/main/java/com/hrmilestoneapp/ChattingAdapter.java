package com.hrmilestoneapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

class ChattingAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChatMessage> chatMessageArrayList;
    String str_sender = "sender";
    String str_receiver = "Receiver";

    public ChattingAdapter(Context context, ArrayList<ChatMessage> chatMessageArrayList) {
        this.context = context;
        this.chatMessageArrayList = chatMessageArrayList;
    }


    @Override
    public int getCount() {
        return chatMessageArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatMessageArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        // inflate the layout for each list row
        ChatMessage chatMessage = (ChatMessage) getItem(position);
        String userType = chatMessageArrayList.get(position).getUserType();
        Log.e("chatadapter", userType);

        convertView = layoutInflater.inflate(R.layout.user_r, null);
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.tv_name);
        textViewItemName.setText(chatMessageArrayList.get(position).getMessageText());

        if (userType.equals("sender")) {
            convertView = layoutInflater.inflate(R.layout.user_r, null);
            TextView textViewItemName1 = (TextView) convertView.findViewById(R.id.tv_name);
            TextView datatime = (TextView) convertView.findViewById(R.id.tv_datetime);

            textViewItemName1.setText(chatMessageArrayList.get(position).getMessageText());
            datatime.setText(chatMessageArrayList.get(position).getTime()+"  "+chatMessageArrayList.get(position).getDate());


        } else if (userType.equals("Receiver")) {
            convertView = layoutInflater.inflate(R.layout.user_chat, null);
            TextView textViewItemName1 = (TextView) convertView.findViewById(R.id.tv_name);
            textViewItemName1.setText(chatMessageArrayList.get(position).getMessageText());
            TextView datatime1 = (TextView) convertView.findViewById(R.id.tv_datetime);
            datatime1.setText(chatMessageArrayList.get(position).getTime()+"  "+chatMessageArrayList.get(position).getDate());


        }


        return convertView;
    }
}
