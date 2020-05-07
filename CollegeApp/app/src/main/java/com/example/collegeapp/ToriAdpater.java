package com.example.collegeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToriAdpater extends RecyclerView.Adapter<ToriAdpater.MyViewHolder> {

    private List<GymModelClass> modelClassList;

    public ToriAdpater(List<GymModelClass> modelClassList){
        this.modelClassList =modelClassList;
    }

    @NonNull
    @Override
    public ToriAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToriAdpater.MyViewHolder holder, int position) {
    int img = modelClassList.get(position).getImage();
    String name = modelClassList.get(position).getTypeName();
    String description = modelClassList.get(position).getDescription();
    holder.setData(img,name,description);
    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView gymImg;
        private TextView gymName;
        private TextView gymDesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gymImg = itemView.findViewById(R.id.img_id);
            gymName = itemView.findViewById(R.id.name_id);
            gymDesc = itemView.findViewById(R.id.des_id);
        }

        public void setData(int img, String name, String description) {
        gymImg.setImageResource(img);
        gymName.setText(name);
        gymDesc.setText(description);
        }
    }
}
