package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.plant.NewPlantRequestDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantRegistrationFragment extends Fragment {
    private PlantTypeListRecordDTO plantType;
    private TextInputEditText name, place, description;
    private TextView type;
    private Button save;
    private Button back;
    private PlantTypeApi plantTypeApi;

    public PlantRegistrationFragment(PlantTypeListRecordDTO plantType) {this.plantType = plantType;}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plant_registration, container, false);

        type = v.findViewById(R.id.plant_type_reg);
        name = v.findViewById(R.id.plant_name_reg);
        place = v.findViewById(R.id.plant_place_reg);
        save = v.findViewById(R.id.btn_save_reg_plant);
        back = v.findViewById(R.id.btn_back_reg_plant);
        description = v.findViewById(R.id.plant_descr_reg);

        type.setText(plantType.getName());
        name.setText(plantType.getName());
        place.setText("Место не указано");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Place = place.getText().toString();
                NewPlantRequestDTO plant = new NewPlantRequestDTO();
                plant.setName(Name);
                plant.setPlace(Place);
                plant.setPlantTypeId(plantType.getId());
                plant.setFloristId(Info.getId());
                plant.setDescription(description.getText().toString());

                plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
                plantTypeApi.createPlant(plant).enqueue(new Callback<NewPlantRequestDTO>() {
                    @Override
                    public void onResponse(Call<NewPlantRequestDTO> call, Response<NewPlantRequestDTO> response) {
                        Toast.makeText(getContext(), "Растение успешно сохранено", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<NewPlantRequestDTO> call, Throwable throwable) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity)getActivity()).replaceFragment1(plantType);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        back.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragment1(plantType));
        return v;
    }
}