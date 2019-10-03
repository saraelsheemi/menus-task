package com.vogella.android.myapplication.network;

import com.vogella.android.myapplication.models.TagItemDetailsResponse;
import com.vogella.android.myapplication.models.TagListResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("tags/{page}")
    Single<TagListResponse> loadTags(@Path(value = "page") int page);

    @GET("/items/{tagName}")
    Single<TagItemDetailsResponse> loadItems(@Path(value = "tagName") String page);
}
