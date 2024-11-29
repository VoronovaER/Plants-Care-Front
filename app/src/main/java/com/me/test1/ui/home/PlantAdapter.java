package com.me.test1.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.test1.R;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.network.ApiClient;
import com.me.test1.network.DownloadFile;
import com.me.test1.network.PlantTypeApi;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder>{

    interface OnClickListener{
        void onClick(PlantListRecordDTO plant, int position);
    }

    private final OnClickListener onClickListener;
    protected List<PlantListRecordDTO> dataset;
    private PlantTypeApi plantTypeApi = ApiClient.getClient().create(PlantTypeApi.class);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView type;
        private final ImageView image;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.plant_item_name);
            type = view.findViewById(R.id.plant_item_type);
            image = view.findViewById(R.id.plant_image);
        }

        public TextView getName() {
            return name;
        }
        public TextView getType() {
            return type;
        }
        public ImageView getImage() {
            return image;
        }
    }

    public PlantAdapter(OnClickListener onClickListener, List<PlantListRecordDTO> dataSet) {
        this.onClickListener = onClickListener;
        dataset = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.florist_plant_item, parent, false);

        return new PlantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlantListRecordDTO plant = dataset.get(position);
        holder.name.setText(plant.getName());
        holder.type.setText(plant.getPlantType());
        if (plant.getUrl().startsWith("http")) {
            Picasso.with(holder.image.getContext())
                    .load(plant.getUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.image);
        }
        else {
            plantTypeApi.getImage(plant.getUrl()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    InputStream body= response.body().byteStream();
                    Log.d("Image", body.toString());
                    DownloadFile downloadFile = new DownloadFile();
                    File file = downloadFile.download(body);
                    if (file != null) {
                        holder.image.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
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
        holder.itemView.setOnClickListener(v -> onClickListener.onClick(dataset.get(position), position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
