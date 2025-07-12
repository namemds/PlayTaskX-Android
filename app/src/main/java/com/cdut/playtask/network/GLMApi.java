package com.cdut.playtask.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GLMApi {
    @POST("api/paas/v4/chat/completions")
    Call<ChatResponse> chat(@Body ChatRequest request);
}

