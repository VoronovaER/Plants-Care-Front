package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlantTypeAdapter extends RecyclerView.Adapter<PlantTypeAdapter.ViewHolder> implements Filterable {
    interface OnClickListener{
        void onClick(PlantTypeListRecordDTO plantType, int position);
    }

    private final OnClickListener onClickListener;
    protected List<PlantTypeListRecordDTO> dataset;
    private List<PlantTypeListRecordDTO> filterDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.rec_pic_plant_type);
            textView = (TextView) view.findViewById(R.id.rec_text_plant_type);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImageView() {return imageView;}
    }
    public PlantTypeAdapter(List<PlantTypeListRecordDTO> dataSet, OnClickListener onClickListener) {

        dataset = dataSet;
        this.onClickListener = onClickListener;
        this.filterDataset = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plant_type_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {
        viewHolder.getTextView().setText(filterDataset.get(position).getName());
        viewHolder.itemView.setOnClickListener(v -> onClickListener.onClick(filterDataset.get(position), position));
        Picasso.with(viewHolder.imageView.getContext())
                .load(filterDataset.get(position).getUrl())
                .fit()
                .centerCrop()
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return filterDataset.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filterDataset = dataset;
                } else {
                    List<PlantTypeListRecordDTO> filteredList = new ArrayList<>();
                    for (PlantTypeListRecordDTO plant : dataset) {
                        if (plant.getName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(plant);
                        }
                        filterDataset = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filterDataset;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filterDataset = (List<PlantTypeListRecordDTO>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
