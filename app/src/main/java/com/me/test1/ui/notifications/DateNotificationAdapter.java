package com.me.test1.ui.notifications;

import static com.me.test1.dto.task.TaskPeriod.DAILY;
import static com.me.test1.dto.task.TaskPeriod.HOURLY;
import static com.me.test1.dto.task.TaskPeriod.MONTHLY;
import static com.me.test1.dto.task.TaskPeriod.WEEKLY;
import static com.me.test1.dto.task.TaskPeriod.YEARLY;
import static com.me.test1.dto.task.TaskType.PLANT_WATERING;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.florist.FloristTaskDTO;
import com.me.test1.dto.task.TaskPeriod;
import com.me.test1.dto.task.TaskRunStatus;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateNotificationAdapter extends RecyclerView.Adapter<DateNotificationAdapter.ViewHolder>{
    public DateNotificationAdapter(DateNotificationAdapter.OnClickListener onClickListener, List<FloristTaskDTO> dataset) {
        this.onClickListener = onClickListener;
        this.dataset = dataset;
    }

    interface OnClickListener{
        void onClick(FloristTaskDTO task, int position);
    }

    private final DateNotificationAdapter.OnClickListener onClickListener;
    private PlantTypeApi plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
    protected List<FloristTaskDTO> dataset;
    @NonNull
    @Override
    public DateNotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_task_item, parent, false);

        return new DateNotificationAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name, type, time;
        private final CheckBox checkBox;
        private ImageView image;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.date_task_item_name);
            type = (TextView) view.findViewById(R.id.date_task_item_plant);
            time = (TextView) view.findViewById(R.id.date_task_time);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            image = (ImageView) view.findViewById(R.id.date_task_image);
        }

        public TextView getName() {
            return name;
        }
        public TextView getPlant() {
            return type;
        }

        public TextView getTime() {
            return time;
        }
        public CheckBox getCheckBox() {return checkBox;}

        public ImageView getImage() {return image;}
    }

    @Override
    public void onBindViewHolder(@NonNull DateNotificationAdapter.ViewHolder holder, int position) {
        FloristTaskDTO task = dataset.get(position);
        holder.getName().setText(task.getTask().getName());
        if (task.getTask().getType() == PLANT_WATERING){
            Picasso.with(holder.getImage().getContext())
                    .load(R.drawable.wat_can)
                    .fit()
                    .centerCrop()
                    .into(holder.getImage());
        }
        String period = "";
        TaskPeriod taskPeriod = task.getTask().getPeriod();
        if (taskPeriod == HOURLY) {
            period = "Каждый час";
        }else if (taskPeriod == DAILY) {
            period = "Ежедневно";
        }else if (taskPeriod == WEEKLY) {
            period = "Еженедельно";
        }else if (taskPeriod == MONTHLY) {
            period = "Ежемесячно";
        }else if(taskPeriod == YEARLY){
            period = "Ежегодно";
        }

        if(task.getTaskRun() != null){
            holder.checkBox.setVisibility(View.VISIBLE);
            if (task.getTaskRun().getStatus().equals(TaskRunStatus.COMPLETED)) {
                holder.getCheckBox().setChecked(true);
                holder.name.setTextColor(Color.GRAY);
                holder.type.setTextColor(Color.GRAY);
                holder.time.setTextColor(Color.GRAY);
            }else{
                holder.getCheckBox().setChecked(false);
                holder.name.setTextColor(Color.BLACK);
                holder.type.setTextColor(Color.BLACK);
                holder.time.setTextColor(Color.BLACK);
            }
        }else{
            holder.getCheckBox().setVisibility(View.GONE);
        }
        holder.getPlant().setText(task.getTask().getPlantName());
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(task, position));
        holder.checkBox.setOnClickListener(v -> {
            if (holder.getCheckBox().isChecked()) {
                plantTypeApi.changeStatus(task.getTaskRun().getId(), TaskRunStatus.COMPLETED).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("Checkbox", "onResponse: " + response.code());
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Log.d("Checkbox", "onResponse: " + throwable.getMessage());
                    }
                });
                holder.name.setTextColor(Color.GRAY);
                holder.type.setTextColor(Color.GRAY);
                holder.time.setTextColor(Color.GRAY);
            }else{
                plantTypeApi.changeStatus(task.getTaskRun().getId(), TaskRunStatus.SUCCESS).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("Checkbox", "onResponse: " + response.code());
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Log.d("Checkbox", "onResponse: " + throwable.getMessage());
                    }
                });
                holder.getCheckBox().setChecked(false);
                holder.name.setTextColor(Color.BLACK);
                holder.type.setTextColor(Color.BLACK);
                holder.time.setTextColor(Color.BLACK);
            }
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = task.getLocalDateTime().toLocalTime().format(formatter);
        holder.getTime().setText(time);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

