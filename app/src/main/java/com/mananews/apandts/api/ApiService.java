package com.mananews.apandts.api;


import com.mananews.apandts.Model_Class.ReporterModel;
import com.mananews.apandts.Model_Class.StatusModel.Example;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("webservices/add_news.php")
    Call<ReporterModel> imageUpload(
            @Body RequestBody files
    );


    @POST("webservices/get_images.php")
    Call<Example> getStatusService(
            @Query("page") String page,
            @Query("img_type") String img_type
    );
}