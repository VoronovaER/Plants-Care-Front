package com.me.test1.ui.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.plant.PlantDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.dto.task.TaskListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;
import com.me.test1.ui.dashboard.PlantTypeAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
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
    private TextView name, place, type;
    private Button btnEdit;
    private Button btnDelete;
    protected RecyclerView.LayoutManager manager;
    protected PlantTasksAdapter adapter;
    List<TaskListRecordDTO> dataset;
    private PlantDTO plantDTO;


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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity)getActivity()).replaceFragmentHome();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        plantTypeApi.getPlant(plant.getId()).enqueue(new Callback<PlantDTO>() {
            @Override
            public void onResponse(Call<PlantDTO> call, Response<PlantDTO> response) {
                if(response.isSuccessful()) {
                    plantDTO = response.body();
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

        btnEdit.setOnClickListener(v1 ->((MainActivity)getActivity()).replaceEditFloristPlantFragment(plantDTO));
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("Вы хотите удалить растение?");
                builder.setTitle("Внимание!");
                builder.setCancelable(true);
                builder.setPositiveButton("Да", (dialog, which) -> {
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
                });
                builder.setNegativeButton("Нет", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        dataset = new ArrayList<>();
        RecyclerView rv = v.findViewById(R.id.plant_tasks_recycler);

        PlantTasksAdapter.OnClickListener clickListener = (task, position) -> Toast.makeText(getContext(), task.getName(), Toast.LENGTH_SHORT).show();

        manager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(manager);
        adapter = new PlantTasksAdapter(clickListener, dataset);
        rv.setAdapter(adapter);

        plantTypeApi.getPlantTasks(plant.getId()).enqueue(new Callback<List<TaskListRecordDTO>>() {

            @Override
            public void onResponse(Call<List<TaskListRecordDTO>> call, Response<List<TaskListRecordDTO>> response) {
                dataset.addAll(response.body());
                rv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<TaskListRecordDTO>> call, Throwable throwable) {
                Toast.makeText(requireContext(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragmentHome());
        return v;
    }
}