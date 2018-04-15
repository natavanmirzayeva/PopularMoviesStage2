package com.project.udacity.popularmoviesstage2.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.project.udacity.popularmoviesstage2.ui.FavoriteMoviesFragment;
import com.project.udacity.popularmoviesstage2.ui.PopularMoviesFragment;
import com.project.udacity.popularmoviesstage2.ui.TopRatedMoviesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabPagerAdapter extends FragmentStatePagerAdapter
{
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private static int NUM_ITEMS = 3;
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public TabPagerAdapter(FragmentManager manager)
    {
        super(manager);
        mFragmentManager = manager;
        mFragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position) {
            case 0:
                PopularMoviesFragment popularMoviesFragment = PopularMoviesFragment.newInstance();
                return popularMoviesFragment;
            case 1:
                TopRatedMoviesFragment topRatedMoviesFragment = TopRatedMoviesFragment.newInstance();
                return topRatedMoviesFragment;
            case 2:
                FavoriteMoviesFragment favoriteMoviesFragment = FavoriteMoviesFragment.newInstance();
                return favoriteMoviesFragment;
            default:
                return null;
        }
        //return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position) {

            case 0:
                return "Popular";
            case 1:
                return "Top Rated";
            case 2:
                return "Favorite";
        }
        return "" + (position);
    }
}

