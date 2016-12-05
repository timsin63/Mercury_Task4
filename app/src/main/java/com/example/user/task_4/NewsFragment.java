package com.example.user.task_4;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by User on 11/23/2016.
 */

public class NewsFragment extends Fragment {

    public static final String TAG = "NEWS_FRAGMENT";
    public static final String EXTRA_PAGE_URI_TAG = "CUR_PAGE";

    private String url;
    private WebView webView;
    ProgressBar progressBar;
    View view = null;

    public NewsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.f_news, container, false);
        }
        webView = (WebView) view.findViewById(R.id.news_web_view);

        webView.getSettings().setJavaScriptEnabled(true);

        WebViewClient webViewClient = new CustomWebViewClient();

        progressBar = (ProgressBar) view.findViewById(R.id.web_view_progress_bar);

        webView.setWebViewClient(webViewClient);

        webView.setPadding(0, 0, 0, 0);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webView.setInitialScale(getScale() / 11);
        if (savedInstanceState == null) {
            if (!TextUtils.isEmpty(url)) {
                webView.loadUrl(url);
            }
        } else {
            webView.restoreState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        webView.saveState(outState);
        NewsListFragment.CURRENT_URL = url;
        super.onSaveInstanceState(outState);
    }

    public void setUrl(String url) {
        this.url = url;
        if (webView != null && !TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }

    }

    int getScale() {
        Display display = ((WindowManager) getView().getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int val = display.getWidth();

        return val;
    }


    class CustomWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            webView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
