package com.me.test1.network;


import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.me.test1.dto.florist.BaseFloristDTO;
import com.me.test1.dto.florist.FloristDTO;
import com.me.test1.dto.florist.FloristTaskDTO;
import com.me.test1.dto.plant.BasePlantDTO;
import com.me.test1.dto.plant.NewPlantRequestDTO;
import com.me.test1.dto.plant.PlantDTO;
import com.me.test1.dto.plant.PlantListRecordDTO;
import com.me.test1.dto.planttype.PlantTypeDTO;
import com.me.test1.dto.planttype.PlantTypeListRecordDTO;
import com.me.test1.dto.task.NewTaskDTO;
import com.me.test1.dto.task.TaskDTO;
import com.me.test1.dto.task.TaskListRecordDTO;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlantTypeApi {
    @GET("/api/v1/plant/types")
    Call<List<PlantTypeListRecordDTO>> getPlantTypes();

    @GET("/api/v1/plant/types/get/{id}")
    Call<PlantTypeDTO> getPlantType(@Path("id") Long id);

    @GET("/api/v1/florist/{floristId}")
    Call<FloristDTO> getFlorist(@Path("floristId") Long id);

    @GET("/api/v1/florist/{floristId}/plant")
    Call<List<PlantListRecordDTO>> getFloristPlants(@Path("floristId") Long id);

    @POST("/api/v1/plant/item")
    Call<NewPlantRequestDTO> createPlant(@Body NewPlantRequestDTO newPlantRequestDTO);

    @GET("/api/v1/plant/item/{id}")
    Call<PlantDTO> getPlant(@Path("id") Long id);

    @DELETE("/api/v1/plant/item/{id}")
    Call<Void> deletePlant(@Path("id") Long id);

    @PUT("/api/v1/florist/{floristId}")
    Call<BaseFloristDTO> updateFlorist(@Path("floristId") Long id, @Body BaseFloristDTO baseFloristDTO);

    @GET("api/v1/plant/item/{id}/tasks")
    Call<List<TaskListRecordDTO>> getPlantTasks(@Path("id") Long id);

    @GET("api/v1/florist/{floristId}/task/{date}")
    Call<List<FloristTaskDTO>> getTaskList(@Path("floristId") Long id, @Path("date") LocalDate date);

    @PUT("api/v1/plant/item/{id}")
    Call<PlantDTO> updatePlant(@Path("id") Long id, @Body BasePlantDTO plantDTO);

    @POST("api/v1/florist")
    Call<FloristDTO> createFlorist(@Body BaseFloristDTO floristDTO);

    @GET("api/v1/florist/email/{email}")
    Call<FloristDTO> getFloristByEmail(@Path("email") String id);

    @PUT("api/v1/florist/{email}/firebase/{firebaseToken}")
    Call<Void> updateFloristFirebaseToken(@Path("firebaseToken") String firebaseToken, @Path("email") String email);

    @POST("api/v1/task/add/{floristId}")
    Call<TaskDTO> createTask(@Path("floristId") Long id, @Body NewTaskDTO taskDTO);
}
