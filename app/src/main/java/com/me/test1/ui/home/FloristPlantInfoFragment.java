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

import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.plant.PlantDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloristPlantInfoFragment extends Fragment {
    private Long floristId;

    private PlantListRecordDTO plant;
    private Button btnBack;
    private PlantTypeApi plantTypeApi;
    private ImageView img;
    private TextView name;
    private TextView place;
    private TextView type;
    private Button btnEdit;
    private Button btnDelete;


    public FloristPlantInfoFragment(PlantListRecordDTO plant, Long floristId) {

        this.plant = plant;
        this.floristId = floristId;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_florist_plant_info, container, false);

        btnBack = v.findViewById(R.id.btn_plant_card_back);
        img = v.findViewById(R.id.plant_card_image);
        name = v.findViewById(R.id.plant_card_name);
        place = v.findViewById(R.id.plant_card_place);
        type = v.findViewById(R.id.plant_card_type);
        btnEdit = v.findViewById(R.id.btn_edit_plant_card);
        btnDelete = v.findViewById(R.id.btn_del_plant_card);

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        plantTypeApi.getPlant(plant.getId()).enqueue(new Callback<PlantDTO>() {
            @Override
            public void onResponse(Call<PlantDTO> call, Response<PlantDTO> response) {
                if(response.isSuccessful()) {
                    PlantDTO plantDTO = response.body();
                    name.setText(plantDTO.getName());
                    place.setText(plantDTO.getPlace());
                    type.setText(plantDTO.getPlantType().getName());
                    if (!Objects.equals(plantDTO.getUrl(), null)) {
                        Picasso.with(requireContext())
                                .load(plantDTO.getUrl())
                                .resize(300,200)
                                .into(img);
                    }else{
                        Picasso.with(requireContext())
                                .load(plantDTO.getPlantType().getUrl())
                                .fit()
                                .centerCrop()
                                .into(img);
                        System.out.println(plantDTO.getUrl());
                    }

                }
            }

            @Override
            public void onFailure(Call<PlantDTO> call, Throwable throwable) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v1 -> Toast.makeText(getContext(), "Редактирование данных", Toast.LENGTH_SHORT).show());
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity)getActivity()).replaceFragmentHome(floristId);
                plantTypeApi.deletePlant(plant.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Удалено", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                //((MainActivity)getActivity()).replaceFragmentHome(floristId);
            }
        });


        btnBack.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragmentHome(floristId));
        return v;
    }
}