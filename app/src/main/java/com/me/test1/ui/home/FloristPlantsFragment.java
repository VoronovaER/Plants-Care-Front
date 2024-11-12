package com.me.test1.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloristPlantsFragment extends Fragment {

    private ImageView image;
    private TextView name;
    private TextView plantsQuantity;
    private Button edit;

    PlantTypeApi plantTypeApi;

    List<PlantListRecordDTO> dataset;
    protected RecyclerView.LayoutManager manager;
    protected PlantAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_florist_plants, container, false);

        image = view.findViewById(R.id.floristAvatar);
        name = view.findViewById(R.id.floristName);
        plantsQuantity = view.findViewById(R.id.floristPlantsQuantity);
        edit = view.findViewById(R.id.btnEditFloristInfo);


        name.setText(Info.getName());
        if (Objects.equals(Info.getAvatar(), null)){
            Picasso.with(requireContext())
                    .load(R.drawable.avatar)
                    .fit()
                    .centerCrop()
                    .into(image);
        }else{
            Picasso.with(requireContext())
                    .load(Info.getAvatar())
                    .fit()
                    .centerCrop()
                    .into(image);
        }

        edit.setOnClickListener(v -> ((MainActivity)getActivity()).replaceFragmentEditFloristInfo());

        dataset = new ArrayList<>();
        RecyclerView rv = view.findViewById(R.id.floristPlantsRecycler);

        PlantAdapter.OnClickListener clickListener = (plant, position) -> ((MainActivity)getActivity()).replaceFragmentPlantCard(plant, Info.getId());

        manager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(manager);
        adapter = new PlantAdapter(clickListener, dataset);
        rv.setAdapter(adapter);
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        plantTypeApi.getFloristPlants(Info.getId()).enqueue(new Callback<List<PlantListRecordDTO>>() {
            @Override
            public void onResponse(Call<List<PlantListRecordDTO>> call, Response<List<PlantListRecordDTO>> response) {
                dataset.addAll(response.body());
                rv.getAdapter().notifyDataSetChanged();
                plantsQuantity.setText(Integer.toString(dataset.size()));

            }

            @Override
            public void onFailure(Call<List<PlantListRecordDTO>> call, Throwable t) {
                Toast.makeText(requireContext(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}