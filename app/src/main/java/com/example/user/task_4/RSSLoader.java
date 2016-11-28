package com.example.user.task_4;

import android.app.Fragment;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by User on 11/23/2016.
 */

public class RSSLoader extends AsyncTask<Fragment, Void, ArrayList<Item>> {

    private static final String TAG = "RSSLoader";

    final URL CHANEL = new URL("https://news.yandex.ru/Samara/index.rss");
    ArrayList<Item> titles = new ArrayList<>();

    RecyclerView recyclerView;
    NewsListFragment newsListFragment;


    public RSSLoader() throws MalformedURLException {
    }


    @Override
    protected ArrayList<Item> doInBackground(Fragment... fragments) {
        try {
            newsListFragment = (NewsListFragment) fragments[0];

            InputStream inputStream = CHANEL.openConnection().getInputStream();
            Feed feed = EarlParser.parse(inputStream, 0);

            for (Item item : feed.getItems()) {
                titles.add(item);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        return titles;
    }


    @Override
    protected void onPostExecute(ArrayList<Item> list) {
        if (newsListFragment.getView() != null) {
            recyclerView = (RecyclerView) newsListFragment.getView().findViewById(R.id.news_list_view);
            if (recyclerView != null) {
                recyclerView.setAdapter(new NewsListAdapter(list, newsListFragment));
            }
        }
    }
}
