package com.example.ashraf.newsapp;
import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;

import java.util.List;
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AsynClass extends AsyncTaskLoader<List<NewsItem>> {
    String HUrl;
    public AsynClass(Context context, String url) {
        super(context);
        HUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<NewsItem> loadInBackground() {
        if (HUrl == null) {
            return null;
        }
        List<NewsItem> News = Quest.fetch(HUrl);
        return News;
    }
}
