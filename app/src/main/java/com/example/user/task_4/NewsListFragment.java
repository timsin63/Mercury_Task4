package com.example.user.task_4;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.task_4.database.DaoSession;
import com.example.user.task_4.database.RssItem;
import com.example.user.task_4.database.RssItemDao;

import java.util.List;

/**
 * Created by User on 11/23/2016.
 */

public class NewsListFragment extends Fragment {

    private RssItemDao noteDao;

    public static final String TAG = "NEWS_LIST_FRAGMENT";
    SwipeRefreshLayout swipeRefreshLayout;
    public static String CURRENT_URL = null;
    NewsListAdapter adapter;
    View view = null;

    RecyclerView newsList;

    public NewsListFragment(){
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.f_news_list, container, false);
        }
        newsList = (RecyclerView) view.findViewById(R.id.news_list_view);

        newsList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        newsList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                RssDownloadService.RssBinder binder = ((MainActivity) getActivity()).binder;
                RssDownloadService rssDownloadService = binder.getService();

                rssDownloadService.downloadRss();

                swipeRefreshLayout.setRefreshing(false);
            }
        });



        BroadcastReceiver onDownloadFinishedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loadDataFromDB();
            }
        };

        getActivity().registerReceiver(onDownloadFinishedReceiver, new IntentFilter(RssDownloadService.DATABASE_UPDATED));

        return view;
    }


    private void loadDataFromDB() {
        DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
        noteDao = daoSession.getRssItemDao();

        List<RssItem> response = noteDao.queryBuilder().where(RssItemDao.Properties.Title.isNotNull()).list();

        adapter = new NewsListAdapter(response);
        newsList.setAdapter(adapter);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((MainActivity) getActivity()).getArticle(CURRENT_URL != null ? CURRENT_URL : response.get(0).getLink());
        }
    }

}
