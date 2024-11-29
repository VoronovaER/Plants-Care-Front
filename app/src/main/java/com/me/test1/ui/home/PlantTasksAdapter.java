package com.me.test1.ui.home;

import static com.me.test1.dto.task.TaskType.*;
import static com.me.test1.dto.task.TaskPeriod.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.task.TaskListRecordDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlantTasksAdapter extends RecyclerView.Adapter<PlantTasksAdapter.ViewHolder>{

    public PlantTasksAdapter(OnClickListener onClickListener, List<TaskListRecordDTO> dataset) {
        this.onClickListener = onClickListener;
        this.dataset = dataset;
    }

    interface OnClickListener{
        void onClick(TaskListRecordDTO task, int position);
    }

    private final PlantTasksAdapter.OnClickListener onClickListener;
    protected List<TaskListRecordDTO> dataset;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_task_item, parent, false);

        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        //private final TextView type;
        private final TextView period;
        private final ImageView img;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.plant_task_item_name);
            //type = (TextView) view.findViewById(R.id.plant_task_item_type);
            period = (TextView) view.findViewById(R.id.plant_task_item_period);
            img = (ImageView) view.findViewById(R.id.plant_task_img);
        }

        public TextView getName() {
            return name;
        }
        public TextView getPeriod() {
            return period;
        }
        public ImageView getImage() {return  img;}
    }

    @Override
    public void onBindViewHolder(@NonNull PlantTasksAdapter.ViewHolder holder, int position) {
        TaskListRecordDTO task = dataset.get(position);
        holder.getName().setText(task.getName());
        if (task.getType() == PLANT_WATERING){
            Picasso.with(holder.getImage().getContext())
                    .load(R.drawable.wat_can)
                    .fit()
                    .centerCrop()
                    .into(holder.getImage());
        }
        String period = "";
        if (task.getPeriod() == HOURLY) {
            period = "Каждый час";
        }else if (task.getPeriod() == DAILY) {
            period = "Ежедневно";
        }else if (task.getPeriod() == WEEKLY) {
            period = "Еженедельно";
        }else if (task.getPeriod() == MONTHLY) {
            period = "Ежемесячно";
        }else if(task.getPeriod() == YEARLY){
            period = "Ежегодно";
        }
        holder.getPeriod().setText(period);
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(task, position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
