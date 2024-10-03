package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;

import java.util.List;

public class PlantTypeAdapter extends RecyclerView.Adapter<PlantTypeAdapter.ViewHolder>{
    interface OnClickListener{
        void onClick(PlantTypeListRecordDTO plantType, int position);
    }

    private final OnClickListener onClickListener;
    protected List<PlantTypeListRecordDTO> dataset;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
    public PlantTypeAdapter(List<PlantTypeListRecordDTO> dataSet, OnClickListener onClickListener) {

        dataset = dataSet;
        this.onClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plant_type_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getTextView().setText(dataset.get(position).getName());
        viewHolder.itemView.setOnClickListener(v -> onClickListener.onClick(dataset.get(position), position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
