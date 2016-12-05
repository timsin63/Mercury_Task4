package com.example.user.task_4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    RssDownloadService rssDownloadService;
    boolean isBounded = false;
    RssDownloadService.RssBinder binder;

    public static final String SHOW_ARTICLE = "Show";
    WebView webView;
    public static int selectedPosition;
    BroadcastReceiver onItemClickReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);

        webView = (WebView) findViewById(R.id.news_web_view);
        fragmentManager = getFragmentManager();

        if (savedInstanceState == null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                selectedPosition = 0;
            } else {
                selectedPosition = -1;
            }
        }
        setOrResetBackButton();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setOrResetBackButton();
            }
        });

        onItemClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getArticle(intent.getStringExtra(NewsListAdapter.EXTRA_URI_TAG));
            }
        };
        registerReceiver(onItemClickReceiver, new IntentFilter(SHOW_ARTICLE));
    }


    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, RssDownloadService.class), serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            binder = (RssDownloadService.RssBinder) iBinder;
            rssDownloadService = binder.getService();
            isBounded = true;
            rssDownloadService.downloadRss();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBounded = false;
        }
    };


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBounded){
            unbindService(serviceConnection);
            isBounded = false;
            unregisterReceiver(onItemClickReceiver);
        }
    }

    private void setOrResetBackButton(){
        int stackHeight = fragmentManager.getBackStackEntryCount();
        if ((stackHeight > 0) && (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
        }
    }

    public void getArticle(String articleUrl) {
        NewsFragment newsFragment = (NewsFragment) fragmentManager.findFragmentByTag(NewsFragment.TAG);
        if (newsFragment == null) {
            newsFragment = new NewsFragment();
        }

        newsFragment.setUrl(articleUrl);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            fragmentTransaction = fragmentManager.beginTransaction()
                    .replace(R.id.a_main, newsFragment, NewsFragment.TAG).addToBackStack(null);
            fragmentTransaction.commit();
        } else {

            webView.setInitialScale(100);
            webView.loadUrl(articleUrl);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                fragmentManager.popBackStack();
                return true;
        }
        return false;
    }
}
