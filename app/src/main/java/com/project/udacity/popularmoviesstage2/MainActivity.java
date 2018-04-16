package com.project.udacity.popularmoviesstage2;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.project.udacity.popularmoviesstage2.utils.TabPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    int mCurrentItem;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fillViewPager();
        if (savedInstanceState != null)
        {
            mCurrentItem = savedInstanceState.getInt("current_item");
            viewPager.setCurrentItem(mCurrentItem);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("current_item", viewPager.getCurrentItem());
    }

    void fillViewPager()
    {
        final TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager());
       /* adapter.addFragment(PopularMoviesFragment.newInstance(), getString(R.string.popular_movies));
        adapter.addFragment(TopRatedMoviesFragment.newInstance(), getString(R.string.top_rated_movies));
        adapter.addFragment(FavoriteMoviesFragment.newInstance(), getString(R.string.favorite_movies)); */

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
      /* viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Fragment fragment = adapter.getItem(position);
                if (fragment != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    getSupportFragmentManager().beginTransaction().show(fragment).commit();
                   //fragment.onResume();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        }); */
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentItem = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                mCurrentItem = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(mCurrentItem);
    }
}
