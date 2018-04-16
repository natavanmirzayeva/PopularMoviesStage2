package com.project.udacity.popularmoviesstage2.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
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
    private  List<Fragment> mFragmentList = new ArrayList<>();
    private  List<String> mFragmentTitleList = new ArrayList<>();

    public TabPagerAdapter(FragmentManager manager)
    {
        super(manager);
    }

    @Override
    public Fragment getItem(int position)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        switch (position)
        {
            case 0: return PopularMoviesFragment.newInstance();
            case 1: return TopRatedMoviesFragment.newInstance();
            case 2: return FavoriteMoviesFragment.newInstance();
            default: return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        int index = 0;
        if(PopularMoviesFragment.newInstance() == object){
            index = 0;
        }else if(TopRatedMoviesFragment.newInstance() == object){
            index = 1;
        }else if(FavoriteMoviesFragment.newInstance() == object){
            index = 2;
        }else{
            index = -1;
        }
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
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

   /* @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        Fragment fragment = mFragmentList.get(position);

        if((obj!=null && fragment!=null) && !(obj.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))){
            destroyItem(container, position, obj);
            return super.instantiateItem(container, position);
        }else{
            return obj;
        }
    } */
}

