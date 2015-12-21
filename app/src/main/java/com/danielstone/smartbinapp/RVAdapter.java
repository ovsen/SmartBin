package com.danielstone.smartbinapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by danielstone on 21/12/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BinViewHolder>{

    public static class BinViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView binTitle;
        TextView binLng;
        TextView binLat;

        BinViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            binTitle = (TextView)itemView.findViewById(R.id.binTitle);
            binLng = (TextView)itemView.findViewById(R.id.binLng);
            binLat = (TextView)itemView.findViewById(R.id.binLat);
        }


    }

    List<FetchDataActivity.BinInfo> binInfoList;


    RVAdapter(List<FetchDataActivity.BinInfo> binInfoList){
        this.binInfoList = binInfoList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BinViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        BinViewHolder pvh = new BinViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BinViewHolder personViewHolder, int i) {
        personViewHolder.binTitle.setText(binInfoList.get(i).ID);
        personViewHolder.binLng.setText(binInfoList.get(i).LNG);
        personViewHolder.binLat.setText(binInfoList.get(i).LAT);
    }

    @Override
    public int getItemCount() {
        return binInfoList.size();
    }

}