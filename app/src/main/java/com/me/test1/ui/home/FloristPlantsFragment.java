package com.me.test1.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.me.test1.Authorization;
import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.DownloadFile;
import com.me.test1.network.PlantTypeApi;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FloristPlantsFragment extends Fragment {

    private ImageView image;
    private TextView name, plantsQuantity;
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
        ImageView menu = view.findViewById(R.id.btn_menu);
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        name.setText(Info.getName());
        if (Objects.equals(Info.getAvatar(), null)){
            Picasso.with(requireContext())
                    .load(R.drawable.avatar)
                    .fit()
                    .centerCrop()
                    .into(image);
        }else {
            plantTypeApi.getImage(Info.getAvatar()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStream body= response.body().byteStream();
                    Log.d("Image", body.toString());
                    DownloadFile downloadFile = new DownloadFile();
                    File file = downloadFile.download(body);
                    if (file != null) {
                        image.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }else{
                        Log.d("Image", "download returned null");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("Image", throwable.getMessage());
                }
            });
        }


        edit.setOnClickListener(v -> ((MainActivity)getActivity()).replaceFragmentEditFloristInfo());

        dataset = new ArrayList<>();
        RecyclerView rv = view.findViewById(R.id.floristPlantsRecycler);

        PlantAdapter.OnClickListener clickListener = (plant, position) -> ((MainActivity)getActivity()).replaceFragmentPlantCard(plant, Info.getId());

        manager = new LinearLayoutManager(requireContext());
        rv.setLayoutManager(manager);
        adapter = new PlantAdapter(clickListener, dataset);
        rv.setAdapter(adapter);


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
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });
        return view;
    }

    private void showMenu(View v){
        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.toolbar_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.logout){
                    Info.setId(null);
                    Info.setName(null);
                    Info.setAvatar(null);
                    Info.setEmail(null);
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    startActivity(new Intent(requireContext(), Authorization.class));
                }
                return true;
            }
        });
        popupMenu.show();
    }
}

