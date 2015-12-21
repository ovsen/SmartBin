package com.danielstone.smartbinapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomList extends CursorAdapter {


    public CustomList(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }
    public static class ViewHolder {
        public final TextView binTitle;
        public final TextView binLat;
        public final TextView binLng;


        public ViewHolder(View view) {
            binLat = (TextView) view.findViewById(R.id.binLat);
            binTitle = (TextView) view.findViewById(R.id.binTitle);
            binLng = (TextView) view.findViewById(R.id.binLng);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(null,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        FetchDataActivity fda = new FetchDataActivity();
        viewHolder.binTitle.setText(fda.fullBinIDs);

    }

}