package com.vogella.android.myapplication.views.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vogella.android.myapplication.R;
import com.vogella.android.myapplication.views.fragments.TagItemsListFragment;
import com.vogella.android.myapplication.network.CacheManager;
import com.vogella.android.myapplication.utils.FragmentSwitchListener;


public class MainActivity extends AppCompatActivity implements FragmentSwitchListener {
    CacheManager cacheManager = new CacheManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onFragmentSwitch(TagItemsListFragment.newInstance());

    }

    @Override
    public void onFragmentSwitch(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    public void addNewTransition(Fragment fragment, ImageView imageView) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .addSharedElement(imageView, ViewCompat.getTransitionName(imageView))
                .addToBackStack(null);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
