package com.sof.testnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sof.testnews.Constants;
import com.sof.testnews.NewsActivity;
import com.sof.testnews.Preferences;
import com.sof.testnews.models.News;
import com.sof.testnews.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Preferences preferences;
    private Context context;
    private List<News> newsList;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    private ArrayList<News> savedNews;

    public NewsAdapter (Context context, ArrayList<News> savedNews){
        this.context = context;
        this.savedNews = savedNews;
        newsList = new LinkedList<>();
        preferences = new Preferences(context);
    }

    public void setData(List<News> news) {
        newsList = news;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new NewsViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        News news = newsList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.tvTitle.setText(news.getTitle());
                newsViewHolder.tvDescription.setText(news.getDescription());
                newsViewHolder.tvTime.setText(Constants.getDate(news.getPublishedAt()));
                Glide.with(context).load(news.getUrlToImage()).apply(RequestOptions.centerCropTransform()).into(newsViewHolder.ivImage);
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == newsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new News());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = newsList.size() - 1;
        News result = getItem(position);

        if (result != null) {
            newsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(News news) {
        newsList.add(news);
        notifyItemInserted(newsList.size() - 1);
    }

    public void addAll(List<News> moveResults) {
        for (News result : moveResults) {
            add(result);
        }
    }

    public News getItem(int position) {
        return newsList.get(position);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.tv_description)
        TextView tvDescription;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.button_save)
        Button buttonSave;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.button_save)
        void saveClick(Button button) {
            Log.e("ASD", "button save clicked " + getAdapterPosition());
            button.setVisibility(View.GONE);
            News news = newsList.get(getAdapterPosition());
            saveNews(news);
        }

        @OnClick
        void onClick(View view) {
            News news = newsList.get(getAdapterPosition());
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("news", news);
            intent.putParcelableArrayListExtra("saved", savedNews);
            context.startActivity(intent);
            Log.e("ASD", news.getTitle());
        }

        private void saveNews(News news) {
            String savedNews = preferences.getSavedNews();
            Gson gson = new Gson();
            if (savedNews.equals("")) {
                ArrayList<News> savedNewsList = new ArrayList<>();
                savedNewsList.add(news);
                String json = gson.toJson(savedNewsList);
                preferences.setSavedNews(json);
            } else {
                String json = preferences.getSavedNews();
                ArrayList<News> savedNewsList = gson.fromJson(json, new TypeToken<List<News>>(){}.getType());
                savedNewsList.add(news);
                json = gson.toJson(savedNewsList);
                preferences.setSavedNews(json);
            }
        }

    }


    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }
}
