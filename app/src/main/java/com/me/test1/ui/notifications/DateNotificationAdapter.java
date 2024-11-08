package com.me.test1.ui.notifications;

import static com.me.test1.dto.task.TaskPeriod.DAILY;
import static com.me.test1.dto.task.TaskPeriod.HOURLY;
import static com.me.test1.dto.task.TaskPeriod.MONTHLY;
import static com.me.test1.dto.task.TaskPeriod.WEEKLY;
import static com.me.test1.dto.task.TaskPeriod.YEARLY;
import static com.me.test1.dto.task.TaskType.PLANT_CHECKING;
import static com.me.test1.dto.task.TaskType.PLANT_FEEDING;
import static com.me.test1.dto.task.TaskType.PLANT_RELOCATION;
import static com.me.test1.dto.task.TaskType.PLANT_REPLANTING;
import static com.me.test1.dto.task.TaskType.PLANT_WATERING;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.florist.FloristTaskDTO;
import com.me.test1.dto.task.TaskPeriod;
import com.me.test1.dto.task.TaskType;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateNotificationAdapter extends RecyclerView.Adapter<DateNotificationAdapter.ViewHolder>{
    public DateNotificationAdapter(DateNotificationAdapter.OnClickListener onClickListener, List<FloristTaskDTO> dataset) {
        this.onClickListener = onClickListener;
        this.dataset = dataset;
    }

    interface OnClickListener{
        void onClick(FloristTaskDTO task, int position);
    }

    private final DateNotificationAdapter.OnClickListener onClickListener;
    protected List<FloristTaskDTO> dataset;
    @NonNull
    @Override
    public DateNotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_task_item, parent, false);

        return new DateNotificationAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView type;
        private final TextView period;
        private final TextView time;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.date_task_item_name);
            type = (TextView) view.findViewById(R.id.date_task_item_plant);
            period = (TextView) view.findViewById(R.id.date_task_item_period);
            time = (TextView) view.findViewById(R.id.date_task_time);
        }

        public TextView getName() {
            return name;
        }
        public TextView getPlant() {
            return type;
        }

        public TextView getPeriod() {
            return period;
        }
        public TextView getTime() {
            return time;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DateNotificationAdapter.ViewHolder holder, int position) {
        FloristTaskDTO task = dataset.get(position);
        holder.getName().setText(task.getTask().getName());
        /*String type = "";
        TaskType taskType = task.getTask().getType();
        if (taskType == PLANT_WATERING){
            type = "Полив";
        }else if (taskType == PLANT_FEEDING){
            type = "Удобрение";
        }else if (taskType == PLANT_REPLANTING){
            type = "Пересаживание";
        }else if (taskType == PLANT_RELOCATION){
            type = "Перемещение";
        }else if (taskType == PLANT_CHECKING){
            type = "Проверка";
        }
        holder.getType().setText(type);*/
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
        holder.getPeriod().setText(period);
        holder.getPlant().setText(task.getTask().getPlantName());
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(task, position));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = task.getLocalDateTime().toLocalTime().format(formatter);
        holder.getTime().setText(time);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
