package com.mananews.apandts.api;


import com.mananews.apandts.Model_Class.ReporterModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("webservices/add_news.php")
    Call<ReporterModel> imageUpload(
            @Body RequestBody files
    );

}