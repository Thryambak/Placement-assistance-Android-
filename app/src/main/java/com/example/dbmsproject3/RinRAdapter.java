package com.example.dbmsproject3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RinRAdapter extends RecyclerView.Adapter<RinRAdapter.myViewHolder> {

    Placed[] myPlaced;
    Context context;

    public RinRAdapter(Context context, Placed[] myPlaced) {
        this.context=context;
        this.myPlaced=myPlaced;


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rinrrow,parent,false);
        return new RinRAdapter.myViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.year.setText(Integer.toString(myPlaced[position].getYear()));
        holder.noPlaced.setText(Integer.toString(myPlaced[position].getNumber()));
        Log.i("NO of RinR",Integer.toString(myPlaced.length));

    }

    @Override
    public int getItemCount() {
        return myPlaced.length;
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView year;
        TextView noPlaced;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            year=itemView.findViewById(R.id.yearEditText);
            noPlaced=itemView.findViewById(R.id.editNopPlaced);
        }
    }

}
