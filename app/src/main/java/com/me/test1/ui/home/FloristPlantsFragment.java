package com.me.test1.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.test1.R;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.planttype.PlantTypeDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloristPlantsFragment extends Fragment {

    private Long floristId;
    private ImageView image;
    private TextView name;
    private TextView plantsQuantity;
    PlantTypeApi plantTypeApi;
    private Button edit;


    public FloristPlantsFragment(Long id) {
        floristId = id;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_florist_plants, container, false);

        image = view.findViewById(R.id.floristAvatar);
        name = view.findViewById(R.id.floristName);
        plantsQuantity = view.findViewById(R.id.floristPlantsQuantity);
        edit = view.findViewById(R.id.btnEditFloristInfo);//??

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        plantTypeApi.getFlorist(floristId).enqueue(new Callback<FloristDTO>() {
            @Override
            public void onResponse(Call<FloristDTO> call, Response<FloristDTO> response) {
                FloristDTO florist = response.body();
                name.setText(florist.getName());
                plantsQuantity.setText(Integer.toString(florist.getPlantsQuantity()));
                Picasso.with(requireContext())
                        .load(florist.getAvatar())
                        .fit()
                        .centerCrop()
                        .into(image);
            }

            @Override
            public void onFailure(Call<FloristDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(v -> Toast.makeText(getContext(), "Edit " + name.getText() + " info", Toast.LENGTH_SHORT).show());
        return view;
    }
}