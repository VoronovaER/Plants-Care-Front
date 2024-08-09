package com.me.test1.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    protected RecyclerView.LayoutManager manager;
    protected PlantTypeAdapter adapter;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        manager = new LinearLayoutManager(requireContext());
        ArrayList<String> dataset = buildList(50);
        adapter = new PlantTypeAdapter(dataset);
        RecyclerView rv = root.findViewById(R.id.recycler);
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
        return root;
    }


    ArrayList<String> buildList(int n){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 1; i <= n; i++){
            list.add(String.format("Категория %d", i));
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}