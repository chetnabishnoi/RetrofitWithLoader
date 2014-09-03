package com.retrofit.service;

import com.retrofit.domain.Item;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RetrofitService {

    @GET("/notes")
    Item getItems();

    @POST("/notes")
    Item postItem(@Body() Item item);
}
