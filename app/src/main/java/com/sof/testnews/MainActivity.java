package com.sof.testnews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sof.testnews.fragments.FragmentEverything;
import com.sof.testnews.fragments.FragmentTopheadliners;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    FragmentTopheadliners fragmentTopheadliners;
    FragmentEverything fragmentEverything;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    public void initUI() {
        ButterKnife.bind(this);
        fragmentEverything = new FragmentEverything();
        fragmentTopheadliners = new FragmentTopheadliners();
        loadFragment(fragmentTopheadliners);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.page_top:
                loadFragment(fragmentTopheadliners);
                break;
            case R.id.page_news:
                loadFragment(fragmentEverything);
                break;
        }
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }



}