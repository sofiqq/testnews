package com.sof.testnews.api;

import com.sof.testnews.models.News;
import com.sof.testnews.models.NewsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsService {
    @GET("top-headlines?q=apple&apiKey=e65ee0938a2a43ebb15923b48faed18d")
    Call<NewsModel> getNews();
}
