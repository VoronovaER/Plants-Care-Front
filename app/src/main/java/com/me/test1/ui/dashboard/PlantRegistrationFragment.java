package com.me.test1.ui.dashboard;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.me.test1.ImageDTO;
import com.me.test1.Info;
import com.me.test1.MainActivity;
import com.me.test1.R;
import com.me.test1.dto.plant.NewPlantRequestDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class PlantRegistrationFragment extends Fragment {
    private PlantTypeListRecordDTO plantType;
    private TextInputEditText name, place, description;
    private TextView type;
    private String imagePath;
    private Button save, back;
    private PlantTypeApi plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
    ImageView image;
    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri o) {
            if (o == null) {
                //Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
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
                                Glide.with(getContext()).load(o).centerCrop().into(image);
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
        image = v.findViewById(R.id.plant_img_reg);
        MaterialButton pickImage = v.findViewById(R.id.pickImage);

        type.setText(plantType.getName());
        name.setText(plantType.getName());
        place.setText("Место не указано");

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

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
                plant.setUrl(imagePath);
                plantTypeApi.createPlant(plant).enqueue(new Callback<NewPlantRequestDTO>() {
                    @Override
                    public void onResponse(Call<NewPlantRequestDTO> call, Response<NewPlantRequestDTO> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getContext(), "Растение успешно сохранено", Toast.LENGTH_SHORT).show();
                            ((MainActivity)getActivity()).replaceFragment1(plantType);
                        }else{
                            Log.d("Image", "UPLOAD: response = " + response.errorBody());
                        }
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