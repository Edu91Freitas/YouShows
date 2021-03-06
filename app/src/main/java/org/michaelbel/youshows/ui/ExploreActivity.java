package org.michaelbel.youshows.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import org.michaelbel.shows.R;
import org.michaelbel.youshows.Theme;
import org.michaelbel.youshows.rest.model.Show;
import org.michaelbel.youshows.ui.fragment.NowPlayingShowsFragment;
import org.michaelbel.youshows.ui.fragment.PopularShowsFragment;
import org.michaelbel.youshows.ui.fragment.TopRatedShowsFragment;
import org.michaelbel.youshows.ui.view.TabView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Date: 19 MAR 2018
 * Time: 13:20 MSK
 *
 * @author Michael Bel
 */

public class ExploreActivity extends AppCompatActivity {

    private final int tab_now_playing = 0;
    private final int tab_popular = 1;
    private final int tab_top_rated = 2;

    private Context context;

    public TabLayout tabs;
    private FragmentsPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        context = ExploreActivity.this;

        getWindow().setStatusBarColor(ContextCompat.getColor(context, Theme.primaryDarkColor()));

        AppBarLayout appBar = findViewById(R.id.app_bar);
        appBar.setBackgroundColor(ContextCompat.getColor(context, Theme.primaryColor()));

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setLayoutParams(AndroidExtensions.setScrollFlags(toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setBackgroundColor(ContextCompat.getColor(context, Theme.primaryColor()));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finish());

        adapter = new FragmentsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NowPlayingShowsFragment());
        adapter.addFragment(new PopularShowsFragment());
        adapter.addFragment(new TopRatedShowsFragment());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                setTabs(position);

                if (position == tab_popular) {
                    PopularShowsFragment fragment = (PopularShowsFragment) adapter.getItem(tab_popular);
                    if (fragment.isAdapterEmpty()) {
                        fragment.loadFirstPage();
                    }
                } else if (position == tab_now_playing) {
                    NowPlayingShowsFragment fragment = (NowPlayingShowsFragment) adapter.getItem(tab_now_playing);
                    if (fragment.isAdapterEmpty()) {
                        fragment.loadFirstPage();
                    }
                } else if (position == tab_top_rated) {
                    TopRatedShowsFragment fragment = (TopRatedShowsFragment) adapter.getItem(tab_top_rated);
                    if (fragment.isAdapterEmpty()) {
                        fragment.loadFirstPage();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabs.setSelectedTabIndicatorColor(ContextCompat.getColor(context, Theme.tabSelectColor()));
        tabs.setBackgroundColor(ContextCompat.getColor(context, Theme.primaryColor()));

        int tab = viewPager.getCurrentItem();

        Objects.requireNonNull(tabs.getTabAt(tab_now_playing)).setCustomView(new TabView(context).setTab(R.string.NowPlaying, R.drawable.ic_play_circle, tab == tab_now_playing));
        Objects.requireNonNull(tabs.getTabAt(tab_popular)).setCustomView(new TabView(context).setTab(R.string.Popular, R.drawable.ic_fire, tab == tab_popular));
        Objects.requireNonNull(tabs.getTabAt(tab_top_rated)).setCustomView(new TabView(context).setTab(R.string.TopRated, R.drawable.ic_star_circle, tab == tab_top_rated));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.Search).setIcon(R.drawable.ic_search)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            .setOnMenuItemClickListener(item -> {
                startActivity(new Intent(context, SearchActivity.class));
                return true;
            });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
    }

    public void startShow(Show show) {
        Intent intent = new Intent(context, ShowActivity.class);
        intent.putExtra("id", show.showId);
        intent.putExtra("name", show.name);
        intent.putExtra("overview", show.overview);
        intent.putExtra("backdropPath", show.backdropPath);
        startActivity(intent);
    }

    private void setTabs(int currentTab) {
        ((TabView) Objects.requireNonNull(Objects.requireNonNull(tabs.getTabAt(tab_now_playing)).getCustomView())).setSelect(currentTab == tab_now_playing);
        ((TabView) Objects.requireNonNull(Objects.requireNonNull(tabs.getTabAt(tab_popular)).getCustomView())).setSelect(currentTab == tab_popular);
        ((TabView) Objects.requireNonNull(Objects.requireNonNull(tabs.getTabAt(tab_top_rated)).getCustomView())).setSelect(currentTab == tab_top_rated);
    }

    private class FragmentsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        private FragmentsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        private void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);
        }
    }
}