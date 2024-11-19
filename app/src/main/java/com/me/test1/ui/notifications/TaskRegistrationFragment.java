package com.me.test1.ui.notifications;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.dto.task.NewTaskDTO;
import com.me.test1.dto.task.TaskDTO;
import com.me.test1.dto.task.TaskPeriod;
import com.me.test1.dto.task.TaskType;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskRegistrationFragment extends Fragment {

    private LocalDate date;
    private PlantTypeApi plantTypeApi;
    private TextInputEditText name;
    private EditText startTime, endDate;
    private AutoCompleteTextView type, period, plant;
    private ArrayList<PlantListRecordDTO> plants= new ArrayList<PlantListRecordDTO>();
    private String[] types = {"Полив", "Удобрение", "Прополка"};
    private String[] periods = {"Каждый час", "Каждый день", "Каждую неделю", "Каждый месяц", "Каждый год"};
    private Button back, save;
    private LocalDateTime startDateTime, endDateTime;
    private int sYear, sMonth, sDay, sHour, sMinute;
    private int eYear, eMonth, eDay, eHour, eMinute;


    public TaskRegistrationFragment(LocalDate date) {
        this.date = date;
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.task_registration, container, false);
        name = v.findViewById(R.id.name_reg_task);
        type = v.findViewById(R.id.type_reg_task);
        period = v.findViewById(R.id.period_reg_task);
        endDate = v.findViewById(R.id.date_end_task);
        plant = v.findViewById(R.id.plant_reg_task);
        back = v.findViewById(R.id.btn_back_reg_task);
        save = v.findViewById(R.id.btn_save_reg_task);
        startTime = v.findViewById(R.id.time_start_task);

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        plantTypeApi.getFloristPlants(Info.getId()).enqueue(new Callback<List<PlantListRecordDTO>>() {
            @Override
            public void onResponse(Call<List<PlantListRecordDTO>> call, Response<List<PlantListRecordDTO>> response) {
                if (response.isSuccessful()) {
                    plants = (ArrayList<PlantListRecordDTO>) response.body();
                    String[] plantsNames = new String[plants.size()];
                    for (int i = 0; i < plants.size(); i++) {
                        plantsNames[i] = plants.get(i).getName();
                    }
                    plant.setThreshold(1);
                    plant.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, plantsNames));
                }
            }

            @Override
            public void onFailure(Call<List<PlantListRecordDTO>> call, Throwable throwable) {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        type.setThreshold(1);
        type.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, types));
        period.setThreshold(1);
        period.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, periods));

        startTime.setOnClickListener(v1 -> startTimePicker());
        endDate.setOnClickListener(v1 -> endDateTimePicker());

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceDateNotificationsFragment();
            }
        });
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    startDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), sHour, sMinute);
                    endDateTime = LocalDateTime.of(eYear, eMonth, eDay, eHour, eMinute);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Неверный формат времени", Toast.LENGTH_SHORT).show();
                    return;
                }
                NewTaskDTO newTaskDTO = new NewTaskDTO();
                newTaskDTO.setName(name.getText().toString());
                newTaskDTO.setType(getTaskType(type.getText().toString()).toString());
                newTaskDTO.setPeriod(getTaskPeriod(period.getText().toString()).toString());
                newTaskDTO.setPlant(plant.getText().toString());
                newTaskDTO.setStartDate(startDateTime);
                newTaskDTO.setEndDate(endDateTime);
                newTaskDTO.setEnabled(true);
                newTaskDTO.setSendNotification(true);

                plantTypeApi.createTask(Info.getId(), newTaskDTO).enqueue(new Callback<TaskDTO>() {
                    @Override
                    public void onResponse(Call<TaskDTO> call, Response<TaskDTO> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Задача успешно создана", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).replaceDateNotificationsFragment();
                        }else{
                            Toast.makeText(getContext(), "Ошибка создания задачи", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskDTO> call, Throwable throwable) {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }

    private TaskType getTaskType(String type) {
        switch (type) {
            case "Полив":
                return TaskType.PLANT_WATERING;
            case "Удобрение":
                return TaskType.PLANT_FEEDING;
            case "Прополка":
                return TaskType.PLANT_POLLING;
            default:
                return null;
        }
    }

    private TaskPeriod getTaskPeriod(String period) {
        switch (period) {
            case "Каждый час":
                return TaskPeriod.HOURLY;
            case "Каждый день":
                return TaskPeriod.DAILY;
            case "Каждую неделю":
                return TaskPeriod.WEEKLY;
            case "Каждый месяц":
                return TaskPeriod.MONTHLY;
            case "Каждый год":
                return TaskPeriod.YEARLY;
            default:
                return null;
        }
    }

    private void startTimePicker() {
        final Calendar c = Calendar.getInstance();
        sHour = c.get(Calendar.HOUR_OF_DAY);
        sMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute) -> {
                    startTime.setText(hourOfDay + ":" + minute);
                    sHour = hourOfDay;
                    sMinute = minute;
                }, sHour, sMinute, true);
        timePickerDialog.show();
    }

    private void endDateTimePicker(){
        final Calendar c = Calendar.getInstance();
        eYear = c.get(Calendar.YEAR);
        eMonth = c.get(Calendar.MONTH);
        eDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {

                    String date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " ";
                    eYear = year;
                    eMonth = monthOfYear+1;
                    eDay = dayOfMonth;
                    endTimePicker(date_time);
                }, eYear, eMonth, eDay);
        datePickerDialog.show();
    }

    private void endTimePicker(String end_date){
        final Calendar c = Calendar.getInstance();
        eHour = c.get(Calendar.HOUR_OF_DAY);
        eMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute) -> {
                    endDate.setText(hourOfDay + ":" + minute + " " + end_date);
                    eHour = hourOfDay;
                    eMinute = minute;
                }, eHour, eMinute, true);
        timePickerDialog.show();
    }
}