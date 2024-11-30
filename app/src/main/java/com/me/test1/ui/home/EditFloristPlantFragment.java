package com.me.test1.ui.home;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.florist.BaseFloristDTO;
import com.me.test1.dto.plant.PlantDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFloristPlantFragment extends Fragment {
    private PlantDTO plant;
    private TextInputEditText name, place, description;
    private Button save;
    private Button back;
    private String imagePath;
    private ImageView imageView;
    private MaterialButton button;
    PlantTypeApi plantTypeApi;
    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {

                imagePath = null;
                Log.d("Image", "UPLOAD: no image selected");
            } else {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContext().getContentResolver().query(o,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Log.d("Image", "UPLOAD: filePath = " + filePath);
                if (filePath != null) {
                    File file = new File(filePath);
                    Log.d("Image", "UPLOAD: file length = " + file.length());
                    Log.d("Image", "UPLOAD: file exist = " + file.exists());
                    Log.d("Image", "UPLOAD: file size= " + file.getTotalSpace());
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    Log.d("Image", "UPLOAD: filePart = " + filePart);
                    plantTypeApi.uploadImage(filePart, Info.getId()).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.d("Image", "UPLOAD: response = " + response.raw());
                                imagePath = response.body();
                                Glide.with(getContext()).load(o).centerCrop().into(imageView);
                            }else if(response.code() == 413){
                                Toast.makeText(getContext(),"Слишком большой размер изображения", Toast.LENGTH_LONG).show();
                            }else{
                                Log.e("Image", "UPLOAD: response = " + response.errorBody());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable throwable) {
                            Log.e("Image", "UPLOAD: throwable = " + throwable.getMessage());
                        }
                    });
                }
            }
        }
    });

    public EditFloristPlantFragment(PlantDTO plant) {
        this.plant = plant;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_florist_plant, container, false);

        name = v.findViewById(R.id.name_edit_plant);
        place = v.findViewById(R.id.place_edit_plant);
        save = v.findViewById(R.id.btn_save_edit_plant);
        back = v.findViewById(R.id.btn_back_edit_plant);
        description = v.findViewById(R.id.descr_edit_plant);
        imageView= v.findViewById(R.id.img_edit_plant);
        button = v.findViewById(R.id.pickImage2);

        name.setText(plant.getName());
        place.setText(plant.getPlace());
        description.setText(plant.getDescription());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        back.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragmentPlantCard(plant.getPlantListRecordDTO(), Info.getId()));
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plant.setName(name.getText().toString());
                plant.setPlace(place.getText().toString());
                plant.setDescription(description.getText().toString());
                plant.setUrl(imagePath);
                plantTypeApi.updatePlant(plant.getId(), plant.getBasePlantDTO()).enqueue(new Callback<PlantDTO>() {

                    @Override
                    public void onResponse(Call<PlantDTO> call, Response<PlantDTO> response) {
                        Toast.makeText(getContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).replaceFragmentPlantCard(plant.getPlantListRecordDTO(), Info.getId());
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