package com.me.test1.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.me.test1.R;
import com.me.test1.databinding.FragmentNotificationsBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private CalendarView calendar;
    private TextView date;
    private String[] months = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября","ноября", "декабря"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        calendar = root.findViewById(R.id.calendar);
        date = root.findViewById(R.id.chosen_date);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();
            date.setText(currentDate.getDayOfMonth() + " " + months[currentDate.getMonthValue()-1] + " " + currentDate.getYear() + " года");
        }

        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String chose_date = dayOfMonth + " " + months[month] + " " + year + " года";
            date.setText(chose_date);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}