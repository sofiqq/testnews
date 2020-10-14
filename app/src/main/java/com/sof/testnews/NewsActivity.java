package com.sof.testnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sof.testnews.models.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.button_unsave)
    Button buttonUnsave;

    private News news;
    private ArrayList<News> savedNews;
    private int id;
    private Gson gson;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        preferences = new Preferences(this);
        gson = new Gson();
        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String json = preferences.getSavedNews();
        savedNews = gson.fromJson(json, new TypeToken<List<News>>(){}.getType());
        id = checkInSaved();
        if (id == -1)
            buttonUnsave.setVisibility(View.GONE);
    }

    private void initUI() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        news = intent.getParcelableExtra("news");
        Glide.with(this).load(news.getUrlToImage()).apply(RequestOptions.centerCropTransform()).into(ivImage);
        tvTitle.setText(news.getTitle());
        tvContent.setText(news.getContent());
        tvDate.setText(Constants.getDate(news.getPublishedAt()));
    }

    private int checkInSaved() {
        if (news != null && savedNews != null) {
            for (int i = 0; i < savedNews.size(); i++) {
                Log.e("ASD", savedNews.get(i).getTitle() + " " + news.getTitle());
                if (savedNews.get(i).getTitle().equals(news.getTitle())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @OnClick(R.id.button_unsave)
    public void onClickUnSave(Button button) {
        button.setVisibility(View.GONE);
        if (id >= 0) {
            savedNews.remove(id);
            String json = gson.toJson(savedNews);
            preferences.setSavedNews(json);
        }
    }



}