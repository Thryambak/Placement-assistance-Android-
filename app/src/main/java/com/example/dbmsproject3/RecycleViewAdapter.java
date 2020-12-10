package com.example.dbmsproject3;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.myViewHolder>{
Context context;
CollegeInfo collegeInfo;

    public RecycleViewAdapter(Context context,CollegeInfo collegeInfo) {
        this.collegeInfo=collegeInfo;
        this.context=context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.collegerow,parent,false);
        Toast.makeText(parent.getContext(), "Here", Toast.LENGTH_SHORT).show();
        return new myViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.branchName.setText("Branch: "+collegeInfo.getBranches()[position].getBranchid().name);
        if(collegeInfo.getBranches()[position].getBranchid().circuit){
            holder.circuit.setText("Circuit Branch");

        }
        else
            holder.circuit.setText("Non Circuit Branch");



        holder.avgPkg.setText("Average package: "+Integer.toString(collegeInfo.getBranches()[position].getAveragePackage())+"₹");
        if(position%2==0){
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#E3C7C7"));
        }
        else
            holder.relativeLayout.setBackgroundColor(Color.parseColor("#D39C9C"));

        Log.i("No  of placed to be displayed",Integer.toString(collegeInfo.getBranches()[position].getPlaced().length));
        if(collegeInfo.getBranches()[position].getPlaced().length==0){
            holder.parentNoPlaced.setVisibility(View.GONE);
            holder.parentYear.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
        }
        else{
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            RinRAdapter rAdapter = new RinRAdapter(context,collegeInfo.getBranches()[position].getPlaced());
            holder.recyclerView.setAdapter(rAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return collegeInfo.getBranches().length;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView branchName,circuit,avgPkg,parentYear,parentNoPlaced;
        RelativeLayout relativeLayout;
        RecyclerView recyclerView;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout= itemView.findViewById(R.id.collegeRowLayout);
            branchName=itemView.findViewById(R.id.BranchName1);
            circuit = itemView.findViewById(R.id.Circuit1);
            avgPkg=itemView.findViewById(R.id.Avgpkg1);
            recyclerView=itemView.findViewById(R.id.RinR);
            parentYear = itemView.findViewById(R.id.parentYear);
            parentNoPlaced= itemView.findViewById(R.id.parentNoPlaced);
        }
    }
}
