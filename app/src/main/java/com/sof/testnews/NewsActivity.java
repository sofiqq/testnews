package com.sof.testnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sof.testnews.models.News;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initUI();
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
}