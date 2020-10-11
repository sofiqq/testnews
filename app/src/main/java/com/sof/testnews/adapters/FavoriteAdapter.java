package com.sof.testnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sof.testnews.Constants;
import com.sof.testnews.NewsActivity;
import com.sof.testnews.Preferences;
import com.sof.testnews.R;
import com.sof.testnews.models.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<News> newsList;
    private Context context;
    private Preferences preferences;

    public FavoriteAdapter (Context context){
        this.context = context;
        preferences = new Preferences(context);
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        holder.buttonSave.setVisibility(View.GONE);
        News news = newsList.get(position);
        holder.tvTitle.setText(news.getTitle());
        Glide.with(context).load(news.getUrlToImage()).apply(RequestOptions.centerCropTransform()).into(holder.ivImage);
        holder.tvDescription.setText(news.getDescription());
        holder.tvTime.setText(Constants.getDate(news.getPublishedAt()));
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick
        void onClick(View view) {
            News news = newsList.get(getAdapterPosition());
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("news", news);
            context.startActivity(intent);
        }
    }

    public void setData(ArrayList<News> newsList) {
        this.newsList = newsList;
    }
}
