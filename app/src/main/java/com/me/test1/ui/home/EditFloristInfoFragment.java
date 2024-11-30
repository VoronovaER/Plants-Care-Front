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
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.PlantTypeApi;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditFloristInfoFragment extends Fragment {

    private TextInputEditText name;
    private Button save, back;
    private ImageView imageView;
    private MaterialButton button;
    PlantTypeApi plantTypeApi;
    private String imagePath;
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


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_florist_info, container, false);
        name = v.findViewById(R.id.name_edit_florist);
        save = v.findViewById(R.id.btn_save_edit_florist);
        back = v.findViewById(R.id.btn_back_edit_florist);
        imageView= v.findViewById(R.id.florist_img_reg);
        button = v.findViewById(R.id.pickImage1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity)getActivity()).replaceFragmentHome();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        name.setText(Info.getName());

        back.setOnClickListener(v1 -> ((MainActivity)getActivity()).replaceFragmentHome());
        plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFloristDTO florist = new FloristDTO();
                florist.setName(name.getText().toString());
                florist.setAvatar(imagePath);
                plantTypeApi.updateFlorist(Info.getId(), florist).enqueue(new Callback<BaseFloristDTO>() {

                    @Override
                    public void onResponse(Call<BaseFloristDTO> call, Response<BaseFloristDTO> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), "Данные успешно сохранены", Toast.LENGTH_SHORT).show();
                            Info.setName(response.body().getName());
                            ((MainActivity)getActivity()).replaceFragmentHome();
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