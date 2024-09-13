package com.me.test1.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.databinding.FragmentDashboardBinding;
import com.me.test1.dto.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardFragment extends Fragment {
    protected RecyclerView.LayoutManager manager;
    protected PlantTypeAdapter adapter;
    List<PlantTypeListRecordDTO> dataset;

    private FragmentDashboardBinding binding;
    PlantTypeApi plantTypeApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        dataset = new ArrayList<>();
        RecyclerView rv = root.findViewById(R.id.recycler);
        PlantTypeAdapter.OnClickListener clickListener = new PlantTypeAdapter.OnClickListener() {
            @Override
            public void onClick(PlantTypeListRecordDTO plantType, int position) {

                Toast.makeText(requireContext(), "Был выбран пункт " + plantType.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        manager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(manager);
        adapter = new PlantTypeAdapter(dataset, clickListener);
        rv.setAdapter(adapter);
        plantTypeApi.getPlantTypes().enqueue(new Callback<List<PlantTypeListRecordDTO>>() {
            @Override
            public void onResponse(Call<List<PlantTypeListRecordDTO>> call, Response<List<PlantTypeListRecordDTO>> response) {
                dataset.addAll(response.body());
                rv.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<PlantTypeListRecordDTO>> call, Throwable t) {
                Toast.makeText(requireContext(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}