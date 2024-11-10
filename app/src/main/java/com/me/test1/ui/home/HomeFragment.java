package com.me.test1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.databinding.FragmentHomeBinding;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public PlantTypeApi plantTypeApi;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        plantTypeApi.getFloristByEmail(Info.getEmail()).enqueue(new Callback<FloristDTO>() {
            @Override
            public void onResponse(Call<FloristDTO> call, Response<FloristDTO> response) {
                Info.setId(response.body().getId());
                Long floristId = Info.getId();
                ((MainActivity)getActivity()).replaceFragmentHome(floristId);
            }

            @Override
            public void onFailure(Call<FloristDTO> call, Throwable throwable) {
                Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}