package com.me.test1.ui.home;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.florist.BaseFloristDTO;
import com.me.test1.dto.plant.PlantDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFloristPlantFragment extends Fragment {
    private PlantDTO plant;
    private TextInputEditText name;
    private TextInputEditText place;
    private Button save;
    private Button back;
    PlantTypeApi plantTypeApi;

    public EditFloristPlantFragment(PlantDTO plant) {
        this.plant = plant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_florist_plant, container, false);

        name = v.findViewById(R.id.name_edit_plant);
        place = v.findViewById(R.id.place_edit_plant);
        save = v.findViewById(R.id.btn_save_edit_plant);
        back = v.findViewById(R.id.btn_back_edit_plant);

        name.setText(plant.getName());
        place.setText(plant.getPlace());

        back.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragmentPlantCard(plant.getPlantListRecordDTO(), Info.getId()));
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plant.setName(name.getText().toString());
                plant.setPlace(place.getText().toString());
                plantTypeApi.updatePlant(plant.getId(), plant.getBasePlantDTO()).enqueue(new Callback<PlantDTO>() {

                    @Override
                    public void onResponse(Call<PlantDTO> call, Response<PlantDTO> response) {
                        Toast.makeText(getContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PlantDTO> call, Throwable throwable) {
                        Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity)getActivity()).replaceFragmentPlantCard(plant.getPlantListRecordDTO(), Info.getId());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }
}