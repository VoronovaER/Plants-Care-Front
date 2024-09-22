package com.me.test1.ui.dashboard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantTypeListFragment extends Fragment {
    protected RecyclerView.LayoutManager manager;
    protected PlantTypeAdapter adapter;
    List<PlantTypeListRecordDTO> dataset;

    PlantTypeApi plantTypeApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        View view = inflater.inflate(R.layout.fragment_plant_type_list, container, false);


        dataset = new ArrayList<>();
        RecyclerView rv = view.findViewById(R.id.recycler);

        PlantTypeAdapter.OnClickListener clickListener = new PlantTypeAdapter.OnClickListener() {
            @Override
            public void onClick(PlantTypeListRecordDTO plantType, int position) {
                ((MainActivity)getActivity()).replaceFragment1(plantType);
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
        return view;
    }
}