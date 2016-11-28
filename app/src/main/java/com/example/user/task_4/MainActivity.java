package com.example.user.task_4;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements NewsListFragment.onNewsChooseListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.news_list_view);

        fragmentManager = getFragmentManager();

        Fragment newsListFragment = fragmentManager.findFragmentByTag(NewsListFragment.TAG);
        if (newsListFragment == null) {
            newsListFragment = new NewsListFragment();
            fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, newsListFragment, NewsListFragment.TAG);
            fragmentTransaction.commit();
        }

    }


    @Override
    public void getArticle(String articleUrl) {
        NewsFragment newsFragment = (NewsFragment) fragmentManager.findFragmentByTag(NewsFragment.TAG);
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }
        newsFragment.setUrl(articleUrl);
        fragmentTransaction = fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newsFragment, NewsFragment.TAG).addToBackStack(NewsFragment.TAG);
        fragmentTransaction.commit();
    }
}
