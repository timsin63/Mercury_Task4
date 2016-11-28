package com.example.user.task_4;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by User on 11/23/2016.
 */

public class NewsFragment extends Fragment {

    public static final String TAG = "NEWS_FRAGMENT";

    private String url;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_news, container, false);

        webView = (WebView) view.findViewById(R.id.news_web_view);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

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
        super.onSaveInstanceState(outState);
    }

    public void setUrl(String url) {
        this.url = url;
        if (webView != null && !TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    private int getScale() {
        Display display = ((WindowManager) getView().getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int val = display.getWidth();

        return val;
    }


}
