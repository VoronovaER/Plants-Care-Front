package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import com.me.test1.dto.planttype.PlantTypeDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantTypeCardFragment extends Fragment {

    private Button btnFrag1, btnAdd;
    private PlantTypeListRecordDTO plantType;
    private TextView name, latinName, description, blossom, temperature, light, watering, feeding;
    private ImageView image;
    PlantTypeApi plantTypeApi;

    public PlantTypeCardFragment(PlantTypeListRecordDTO plantType) {
        this.plantType = plantType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_type_card, container, false);

        name = view.findViewById(R.id.PlantTypeName);
        latinName = view.findViewById(R.id.PlantTypeLatinName);
        description = view.findViewById(R.id.PlantTypeDescription);
        image = view.findViewById(R.id.PlantTypeImage);
        blossom = view.findViewById(R.id.PlantTypeBlossom);
        light = view.findViewById(R.id.PlantTypeLight);
        temperature = view.findViewById(R.id.PlantTypeTemperature);
        watering = view.findViewById(R.id.PlantTypeWatering);
        feeding = view.findViewById(R.id.PlantTypeFeeding);

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        plantTypeApi.getPlantType(plantType.getId()).enqueue(new Callback<PlantTypeDTO>() {
            @Override
            public void onResponse(Call<PlantTypeDTO> call, Response<PlantTypeDTO> response) {
                if (response.isSuccessful()) {
                    PlantTypeDTO plantTypeDTO = response.body();
                    name.setText(plantTypeDTO.getName());
                    latinName.setText(plantTypeDTO.getLatinName());
                    description.setText(plantTypeDTO.getDescription());
                    blossom.setText("Цветение: " + plantTypeDTO.getBlossom().toLowerCase());
                    temperature.setText(String.format("Температура: лето: %d - %d, зима: %d - %d", plantTypeDTO.getSumTempMin(), plantTypeDTO.getSumTempMax(), plantTypeDTO.getWinTempMin(), plantTypeDTO.getWinTempMax()));
                    light.setText(String.format("Свет: %s", plantTypeDTO.getLight().toLowerCase()));
                    watering.setText(String.format("Полив: лето - %s, зима - %s", plantTypeDTO.getSumWatering().toLowerCase(), plantTypeDTO.getWinWatering().toLowerCase()));
                    feeding.setText(String.format("Удобрение: %s раза в месяц", plantTypeDTO.getFertilize()));
                    Picasso.with(requireContext())
                            .load(plantTypeDTO.getUrl())
                            .fit()
                            .centerCrop()
                            .into(image);
                }
            }

            @Override
            public void onFailure(Call<PlantTypeDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
        btnFrag1 = view.findViewById(R.id.btnFrag1);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnFrag1.setOnClickListener(v -> ((MainActivity) getActivity()).replaceFragment2());
        btnAdd.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragmentRegistration(plantType);
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity) getActivity()).replaceFragment2();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }
}