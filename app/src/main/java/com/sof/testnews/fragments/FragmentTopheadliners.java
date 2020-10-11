package com.sof.testnews.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sof.testnews.models.News;
import com.sof.testnews.adapters.NewsAdapter;
import com.sof.testnews.PaginationScrollListener;
import com.sof.testnews.R;
import com.sof.testnews.api.ClientApi;
import com.sof.testnews.api.NewsService;
import com.sof.testnews.models.NewsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTopheadliners extends Fragment {

    private NewsAdapter paginationAdapter;
    private NewsService newsService;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.rv_top)
    RecyclerView rvNews;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, null);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        ButterKnife.bind(this, view);
        newsService = ClientApi.getClient().create(NewsService.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        paginationAdapter = new NewsAdapter(getActivity());
        rvNews.setLayoutManager(linearLayoutManager);
        rvNews.setAdapter(paginationAdapter);

        rvNews.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadNextPage() {

        newsService.getNews().enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                paginationAdapter.removeLoadingFooter();
                isLoading = false;

                NewsModel results = response.body();
                paginationAdapter.addAll(results.getArticles());

                if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadFirstPage() {

        newsService.getNews().enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                Log.e("ASD", call.request().url().toString());
                Log.e("ASD", response.body().getStatus() + " " + response.body().getTotalResults());
                List<News> news = response.body().getArticles();
                Log.e("ASD", news.size() + "");
                Log.e("ASD", news.get(0).getTitle());
                NewsModel results = response.body();
                progressBar.setVisibility(View.GONE);
                paginationAdapter.addAll(results.getArticles());

                if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

            }

        });
    }


}
