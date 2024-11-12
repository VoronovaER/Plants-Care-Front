package com.me.test1.ui.home;

import android.annotation.SuppressLint;
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
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFloristInfoFragment extends Fragment {

    private TextInputEditText name;
    private TextInputEditText email;
    private Button save;
    private Button back;
    PlantTypeApi plantTypeApi;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_florist_info, container, false);
        name = v.findViewById(R.id.name_edit_florist);
        email = v.findViewById(R.id.email_edit_florist);
        save = v.findViewById(R.id.btn_save_edit_florist);
        back = v.findViewById(R.id.btn_back_edit_florist);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity)getActivity()).replaceFragmentHome();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        name.setText(Info.getName());
        email.setText(Info.getEmail());

        back.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragmentHome());
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFloristDTO florist = new FloristDTO();
                florist.setName(name.getText().toString());
                florist.setEmail(email.getText().toString());
                plantTypeApi.updateFlorist(Info.getId(), florist).enqueue(new Callback<BaseFloristDTO>() {

                    @Override
                    public void onResponse(Call<BaseFloristDTO> call, Response<BaseFloristDTO> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
                            Info.setName(response.body().getName());
                        }else{
                            Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseFloristDTO> call, Throwable throwable) {
                        Toast.makeText(getContext(), "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }
}