package com.example.user.task_4;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageButton;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.example.user.task_4.database.DaoSession;
import com.example.user.task_4.database.RssItem;
import com.example.user.task_4.database.RssItemDao;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by User on 11/29/2016.
 */



public class RssDownloadService extends Service {

    final static String CHANEL = "https://news.yandex.ru/Samara/index.rss";
    final static String TAG = "RssDownloadService";
    final static String DATABASE_UPDATED = "updated";
    private final IBinder binder = new RssBinder();
    private RssItemDao noteDao;

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }


    public void downloadRss(){

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = new URL(CHANEL).openConnection().getInputStream();
                    Feed feed = EarlParser.parse(inputStream, 0);

                    DaoSession daoSession = ((App) getApplication()).getDaoSession();
                    noteDao = daoSession.getRssItemDao();
                    noteDao.deleteAll();

                    long key = 0;
                    for (Item item : feed.getItems()){
                        RssItem rssItem = new RssItem(key, item.getTitle(), item.getLink());
                        noteDao.insert(rssItem);
                        key++;

                    }

                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }

                sendBroadcast(new Intent(DATABASE_UPDATED));

                stopSelf();
            }

        };

        thread.start();
        }



    public class RssBinder extends Binder{

        RssDownloadService getService(){
            return RssDownloadService.this;
        }

    }
}
