package com.me.test1.network;

import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.planttype.PlantTypeDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlantTypeApi {
    @GET("/api/v1/plant/types")
    Call<List<PlantTypeListRecordDTO>> getPlantTypes();

    @GET("/api/v1/plant/types/get/{id}")
    Call<PlantTypeDTO> getPlantType(@Path("id") Long id);

    @GET("/api/v1/florist/{floristId}")
    Call<FloristDTO> getFlorist(@Path("floristId") Long id);

}
