package com.abctech.blogtalking.api;


import com.abctech.blogtalking.model.BTBlogEntry;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

public interface BTRestService {
    String BASE_URL = "http://104.199.203.50:9000/blogtalk/api/";

    /***********************************************************************************************
     * Blogs
     **********************************************************************************************/
    @Multipart
    @POST("images")
    Observable<ResponseBody> postImage(@PartMap Map<String, RequestBody> data);

    @POST("blogs")
    Observable<BTBlogEntry> postOrder(@Body BTBlogEntry order);

    @GET("blogs")
    Observable<List<BTBlogEntry>> getBlogEntryList(@Query("sort") String sortBy, @Query("offset") int offset);
}