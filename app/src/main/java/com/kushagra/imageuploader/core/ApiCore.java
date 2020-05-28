package com.kushagra.imageuploader.core;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Kushagra on 5/28/2020.
 */
public class ApiCore {

    private static String BASE_URL = "https://nfjsproject.herokuapp.com/";

    public static Retrofit getRetrofit() {

    OkHttpClient client = new OkHttpClient.Builder()
                        .readTimeout(1, TimeUnit.MINUTES)
                        .build();


        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public interface GenericDefinitionInterface{

        @Multipart
        @POST("nsfw")
        Call<JsonArray> uploadFile(@Part MultipartBody.Part image);


//        Call<JsonArray> because the response is in ArrayFormat
//          If possible please change to object format with API headers and Success code
        /*
        *
        * [
    {
        "className": "Hentai",
        "probability": 0.6761319637298584
    },
    {
        "className": "Drawing",
        "probability": 0.31481942534446716
    },
    {
        "className": "Porn",
        "probability": 0.00796517077833414
    },
    {
        "className": "Neutral",
        "probability": 0.0007100717048160732
    },
    {
        "className": "Sexy",
        "probability": 0.0003732735349331051
    }
]
        *
        * */
    }
}
