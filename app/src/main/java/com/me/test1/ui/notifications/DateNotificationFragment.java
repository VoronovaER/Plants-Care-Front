package com.me.test1.ui.notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.florist.FloristTaskDTO;
import com.me.test1.dto.task.TaskListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.me.test1.ui.home.PlantTasksAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DateNotificationFragment extends Fragment {
    private CalendarView calendar;
    private TextView date;
    private String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября","ноября", "декабря"};
    private PlantTypeApi plantTypeApi;
    protected RecyclerView.LayoutManager manager;
    protected DateNotificationAdapter adapter;
    private Button create;
    List<FloristTaskDTO> dataset = new ArrayList<>();
    LocalDate chosenDate;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_date_notification, container, false);

        calendar = v.findViewById(R.id.calendar);
        date = v.findViewById(R.id.chosen_date);
        create = v.findViewById(R.id.btn_create_task);

        chosenDate = LocalDate.now();
        date.setText(chosenDate.getDayOfMonth() + " " + months[chosenDate.getMonthValue()-1] + " " + chosenDate.getYear() + " года");

        RecyclerView rv = v.findViewById(R.id.date_notification_recycler);

        DateNotificationAdapter.OnClickListener clickListener = (task, position) ->
                Toast.makeText(getContext(), task.getTask().getName(), Toast.LENGTH_SHORT).show();

        manager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(manager);
        adapter = new DateNotificationAdapter(clickListener, dataset);
        rv.setAdapter(adapter);

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        changedDate(rv, chosenDate.getYear(), chosenDate.getMonthValue(), chosenDate.getDayOfMonth());

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String chose_date = dayOfMonth + " " + months[month] + " " + year + " года";
            date.setText(chose_date);
            chosenDate = LocalDate.of(year, month+1, dayOfMonth);
            changedDate(rv, year, month+1, dayOfMonth);
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceTaskRegistrationFragment(chosenDate);
            }
        });

        return v;
    }

    void changedDate(RecyclerView rv, int year, int month, int dayOfMonth){
        dataset.clear();
        plantTypeApi.getTaskList(Info.getId(), LocalDate.of(year, month, dayOfMonth)).enqueue(new Callback<List<FloristTaskDTO>>() {
            @Override
            public void onResponse(Call<List<FloristTaskDTO>> call, Response<List<FloristTaskDTO>> response) {
                dataset.addAll(response.body());
                rv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<FloristTaskDTO>> call, Throwable throwable) {
                Toast.makeText(requireContext(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
    }
}