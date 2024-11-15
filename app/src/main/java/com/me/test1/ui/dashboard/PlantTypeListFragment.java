package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantTypeListFragment extends Fragment {
    protected PlantTypeAdapter adapter;
    List<PlantTypeListRecordDTO> dataset;
    private RecyclerView rv;
    private SearchView search;

    PlantTypeApi plantTypeApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        View view = inflater.inflate(R.layout.fragment_plant_type_list, container, false);


        dataset = new ArrayList<>();
        rv = view.findViewById(R.id.recycler);
        search = view.findViewById(R.id.search);

        PlantTypeAdapter.OnClickListener clickListener = new PlantTypeAdapter.OnClickListener() {
            @Override
            public void onClick(PlantTypeListRecordDTO plantType, int position) {
                ((MainActivity)getActivity()).replaceFragment1(plantType);
            }
        };
        adapter = new PlantTypeAdapter(dataset, clickListener);
        rv.setAdapter(adapter);
        GridLayoutManager layoutManager=new GridLayoutManager(requireContext(),2);
        rv.setLayoutManager(layoutManager);
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
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                adapter.getFilter().filter(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {
                adapter.getFilter().filter(queryString);
                return false;
            }
        });
        return view;
    }
}