package com.example.gainyourmuscle;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GymAdpater extends RecyclerView.Adapter<GymAdpater.ViewHolder> {

    //For cardsview
    private ArrayList<GymModel> gymModelClasses= new ArrayList<>();
    private HomeFragment context;


    public GymAdpater(ArrayList<GymModel> gymModelClasses, HomeFragment context) {
        this.gymModelClasses = gymModelClasses;
        this.context = context;

    }

    @NonNull
    @Override
    public GymAdpater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new GymAdpater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymAdpater.ViewHolder holder, final int position) {

        holder.gym_name.setText(gymModelClasses.get(position).getName());
        holder.gym_desc.setText(gymModelClasses.get(position).getDesc());
        Picasso.get().load(gymModelClasses.get(position).getImage()).into(holder.gym_image);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),Main2Activity.class);
                intent.putExtra("name",gymModelClasses.get(position).getName());
                intent.putExtra("desc",gymModelClasses.get(position).getDesc());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gymModelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        //id tane kaam garnu parxa
        private ImageView gym_image;
        private TextView gym_name;
        private TextView gym_desc;
         RelativeLayout relative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gym_image = itemView.findViewById(R.id.gym_image);
            gym_name = itemView.findViewById(R.id.gym_name);
            gym_desc = itemView.findViewById(R.id.gym_desc);

            relative = itemView.findViewById(R.id.relative);

        }

    }

    public void filterList(ArrayList<GymModel> filteredList){
        gymModelClasses = filteredList;
        notifyDataSetChanged();
    }


}
