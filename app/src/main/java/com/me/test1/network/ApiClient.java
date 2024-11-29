package com.me.test1.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;
import java.util.Base64;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    public static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder().build();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(byte[].class, (JsonSerializer<byte[]>) (src, typeOfSrc, context) -> new JsonPrimitive(Base64.getEncoder().encodeToString(src)))
                    .registerTypeAdapter(byte[].class, (JsonDeserializer<byte[]>) (json, typeOfT, context) -> Base64.getDecoder().decode(json.getAsString()))
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
//                    .baseUrl("https://back.plants-care.ru/")
                    .baseUrl("http://192.168.134.61:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
