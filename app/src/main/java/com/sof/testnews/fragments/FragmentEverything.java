package com.sof.testnews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sof.testnews.Preferences;
import com.sof.testnews.R;
import com.sof.testnews.adapters.FavoriteAdapter;
import com.sof.testnews.adapters.NewsAdapter;
import com.sof.testnews.models.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentEverything extends Fragment {

    Preferences preferences;
    private FavoriteAdapter favoriteAdapter;
    private ArrayList<News> newsList;

    @BindView(R.id.rv_everything)
    RecyclerView rvNews;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_everything, null);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        ButterKnife.bind(this, view);
        preferences = new Preferences(getActivity());
        Gson gson = new Gson();
        String json = preferences.getSavedNews();
        newsList = gson.fromJson(json, new TypeToken<List<News>>(){}.getType());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        favoriteAdapter = new FavoriteAdapter(getActivity());
        favoriteAdapter.setData(newsList);
        rvNews.setLayoutManager(linearLayoutManager);
        rvNews.setAdapter(favoriteAdapter);
    }
}
