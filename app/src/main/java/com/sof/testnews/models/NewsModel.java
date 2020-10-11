package com.sof.testnews.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsModel implements Parcelable {
    private String status;
    private int totalResults;
    private ArrayList<News> articles;

    public NewsModel() {
    }

    public NewsModel(String status, int totalResults, ArrayList<News> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    protected NewsModel(Parcel in) {
        status = in.readString();
        totalResults = in.readInt();
        articles = in.createTypedArrayList(News.CREATOR);
    }

    public static final Creator<NewsModel> CREATOR = new Creator<NewsModel>() {
        @Override
        public NewsModel createFromParcel(Parcel in) {
            return new NewsModel(in);
        }

        @Override
        public NewsModel[] newArray(int size) {
            return new NewsModel[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<News> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<News> articles) {
        this.articles = articles;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeInt(totalResults);
        dest.writeTypedList(articles);
    }
}
