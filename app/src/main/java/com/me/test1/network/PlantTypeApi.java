package com.me.test1.network;

import com.me.test1.dto.PlantTypeDTO;
import com.me.test1.dto.PlantTypeListRecordDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlantTypeApi {
    @GET("/api/v1/plant/types")
    Call<List<PlantTypeListRecordDTO>> getPlantTypes();

    @GET("/api/v1/plant/types/get/{id}")
    Call<PlantTypeDTO> getPlantType(@Path("id") Long id);

}
