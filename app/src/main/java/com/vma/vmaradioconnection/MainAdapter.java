package com.vma.vmaradioconnection;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by Mochamad Rezza Gumilang on 15/02/2022
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.AdapterView>{

    private final ArrayList<Bundle> mList;

    public MainAdapter(ArrayList<Bundle> list){
        this.mList = list;
    }


    @NonNull
    @Override
    public AdapterView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_main, parent, false);
        return new AdapterView(itemView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterView holder, int position) {
        final Bundle data = mList.get(position);
        String name = data.getString("name") + " ("+data.getString("type")+")";
        holder.txvw_id.setText(data.getString("id"));
        holder.txvw_name.setText(name);
        if (data.getString("type").equals("Unknown")){
            holder.card_main.setCardBackgroundColor(Color.parseColor("#EEDDDD"));
        }
        else {
            holder.card_main.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class AdapterView extends RecyclerView.ViewHolder{
        TextView txvw_name,txvw_id;
        CardView card_main;

        public AdapterView(@NonNull View itemView) {
            super(itemView);
            txvw_name = itemView.findViewById(R.id.txvw_name);
            txvw_id = itemView.findViewById(R.id.txvw_id);
            card_main = itemView.findViewById(R.id.card_main);
        }
    }
}
