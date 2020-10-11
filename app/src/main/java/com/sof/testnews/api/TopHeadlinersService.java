package com.sof.testnews.api;

import com.sof.testnews.models.News;
import com.sof.testnews.models.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TopHeadlinersService {
    @GET("{topic}?q=apple&apiKey=e65ee0938a2a43ebb15923b48faed18d&pageSize=5")
    Call<NewsModel> getNews(@Path("topic") String topic, @Query("page") int page);
}
