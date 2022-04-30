package com.app.liftlo.utils;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadApi {

    @Multipart
    @POST(ServerURL.upload_files)
    Call<ResponseData> uploadData(@PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> fileList);


    @Multipart
    @POST(ServerURL.register_driver)
    Call<ResponseData> uploadDriver(@PartMap Map<String, RequestBody> map, @Part List<MultipartBody.Part> fileList);


}
