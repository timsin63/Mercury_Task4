package com.example.user.task_4;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by User on 11/23/2016.
 */

public class NewsListFragment extends Fragment {

    public static final String TAG = "NEWS_LIST_FRAGMENT";

    public interface onNewsChooseListener{
        void getArticle(String newsAddress);
    }

    static onNewsChooseListener newsChooseListener;
    RecyclerView newsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_news_list,  container, false);

        newsChooseListener = (onNewsChooseListener) getActivity();

        newsList = (RecyclerView) view.findViewById(R.id.news_list_view);

        loadRSS();

        newsList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        newsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }



    public void loadRSS(){
        try {
            RSSLoader rssLoader = new RSSLoader();
            rssLoader.execute(this);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void chooseNews(String newsAddress) {
        newsChooseListener.getArticle(newsAddress);
    }
}
