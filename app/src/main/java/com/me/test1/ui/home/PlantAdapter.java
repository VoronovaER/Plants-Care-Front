package com.me.test1.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder>{

    interface OnClickListener{
        void onClick(PlantListRecordDTO plant, int position);
    }

    private final OnClickListener onClickListener;
    protected List<PlantListRecordDTO> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView type;
        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.plant_item_name);
            type = view.findViewById(R.id.plant_item_type);
            image = view.findViewById(R.id.plant_image);
        }

        public TextView getName() {
            return name;
        }
        public TextView getType() {
            return type;
        }
        public ImageView getImage() {
            return image;
        }
    }

    public PlantAdapter(OnClickListener onClickListener, List<PlantListRecordDTO> dataSet) {
        this.onClickListener = onClickListener;
        dataset = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.florist_plant_item, parent, false);

        return new PlantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantListRecordDTO plant = dataset.get(position);
        holder.name.setText(plant.getName());
        holder.type.setText(plant.getPlantType());
        Picasso.with(holder.image.getContext())
                .load(plant.getUrl())
                .fit()
                .centerCrop()
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(dataset.get(position), position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
