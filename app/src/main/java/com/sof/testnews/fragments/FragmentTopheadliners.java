package com.sof.testnews.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sof.testnews.Preferences;
import com.sof.testnews.models.News;
import com.sof.testnews.adapters.NewsAdapter;
import com.sof.testnews.PaginationScrollListener;
import com.sof.testnews.R;
import com.sof.testnews.api.ClientApi;
import com.sof.testnews.api.TopHeadlinersService;
import com.sof.testnews.models.NewsModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sof.testnews.Constants.CURRENT_PAGE_KEY;
import static com.sof.testnews.Constants.LAST_PAGE_KEY;
import static com.sof.testnews.Constants.NEWS_KEY;
import static com.sof.testnews.Constants.TOTAL_PAGES_KEY;

public class FragmentTopheadliners extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private NewsAdapter paginationAdapter;
    private TopHeadlinersService topHeadlinersService;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.rv_top)
    RecyclerView rvNews;

    @BindView(R.id.layout_content)
    LinearLayout layoutContent;

    @BindView(R.id.layout_loading)
    FrameLayout layoutLoading;

    @BindView(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private int type = 0;
    private String topic;

    private NewsModel newsModel;
    private ArrayList<News> savedNews;
    private Preferences preferences;
    private  Gson gson;

    public FragmentTopheadliners(int type, ArrayList<News> savedNews) {
        this.type = type;
        this.savedNews = savedNews;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("ASD", "FragmentTopheadliners onCreateView");
        View view = inflater.inflate(R.layout.fragment_top, null);
        initUI(view);
        preferences = new Preferences(getActivity());
        gson = new Gson();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(NEWS_KEY, newsModel);
        //Log.e("ASD", "before reload size = " + newsModel.getArticles().size());
        outState.putBoolean(LAST_PAGE_KEY, isLastPage);
        outState.putInt(TOTAL_PAGES_KEY, TOTAL_PAGES);
        outState.putInt(CURRENT_PAGE_KEY, currentPage);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            isLastPage = savedInstanceState.getBoolean(LAST_PAGE_KEY);
            newsModel = (NewsModel) savedInstanceState.getParcelable(NEWS_KEY);
            TOTAL_PAGES = savedInstanceState.getInt(TOTAL_PAGES_KEY);
            currentPage = savedInstanceState.getInt(CURRENT_PAGE_KEY);
        }
    }

    private void initUI(View view) {
        ButterKnife.bind(this, view);
        switch(type) {
            case 0:
                topic = "top-headlines";
                break;
            case 1:
                topic = "everything";
                break;
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        topHeadlinersService = ClientApi.getClient().create(TopHeadlinersService.class);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        paginationAdapter = new NewsAdapter(getActivity(), savedNews);
        if (newsModel != null) {
            Log.e("ASD", "FragmentTopheadliners initUI newsmodel != null");
            Log.e("ASD", "FragmentTopheadliners initUI newsModel size = " + newsModel.getArticles().size());

            paginationAdapter.setData(newsModel.getArticles());
            layoutContent.setVisibility(View.VISIBLE);
            layoutLoading.setVisibility(View.GONE);
            if (currentPage < TOTAL_PAGES)
                paginationAdapter.addLoadingFooter();
        }
        rvNews.setLayoutManager(linearLayoutManager);
        rvNews.setAdapter(paginationAdapter);
        paginationAdapter.notifyDataSetChanged();

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
        if (newsModel == null)
            loadFirstPage(false);
    }

    private void loadNextPage() {
        topHeadlinersService.getNews(topic, currentPage).enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                paginationAdapter.removeLoadingFooter();
                isLoading = false;
                NewsModel results = response.body();
                ArrayList<News> articles = new ArrayList<>();
                articles.addAll(newsModel.getArticles());
                articles.addAll(results.getArticles());
                paginationAdapter.addAll(results.getArticles());
                newsModel.setArticles(articles);
                if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void loadFirstPage(boolean closeSwipeRefresh) {
        Log.e("ASD", "FragmentTopheadliners loadFirst page topic = " + topic);
        topHeadlinersService.getNews(topic, currentPage).enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                layoutContent.setVisibility(View.VISIBLE);
                layoutLoading.setVisibility(View.GONE);
                int totalResults = response.body().getTotalResults();
                TOTAL_PAGES = response.body().getTotalResults() / 5;
                if (totalResults % 5 > 0) TOTAL_PAGES++;
                if (closeSwipeRefresh && response.body() != null &&
                        response.body().getArticles() != null &&
                        response.body().getArticles().size() > 0 &&
                        !response.body().getArticles().get(0).getTitle().equals(
                                newsModel.getArticles().get(0).getTitle())) {
                    newsModel = response.body();
                    paginationAdapter.addAll(newsModel.getArticles());
                    paginationAdapter.notifyDataSetChanged();
                } else  if (!closeSwipeRefresh){
                    newsModel = response.body();
                    paginationAdapter.addAll(newsModel.getArticles());
                    paginationAdapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
                if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                else isLastPage = true;
                if (closeSwipeRefresh)
                    mSwipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String json = preferences.getSavedNews();
        savedNews = gson.fromJson(json, new TypeToken<List<News>>(){}.getType());
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        loadFirstPage(true);
    }
}
