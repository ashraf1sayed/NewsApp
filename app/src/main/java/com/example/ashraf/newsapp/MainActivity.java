package com.example.ashraf.newsapp;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {
    private String REQUEST_URL = " https://content.guardianapis.com/search?api-key=b653caeb-f5c6-452d-aa1d-1b3a9b22e033&show-tags=contributor";
    private LoaderManager loaderManager;
    private static int loaded_News_id = 1;
    private myAdapter mAdapter;
    private ProgressBar progressbar;
    ListView listView;
    TextView NoFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        NoFound = (TextView) findViewById(R.id.no_found);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        listView.setEmptyView(NoFound);
        mAdapter = new myAdapter(this, new ArrayList<NewsItem>());
        listView.setAdapter(mAdapter);
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager = getLoaderManager();
            loaderManager.initLoader(loaded_News_id, null, MainActivity.this);
        } else {
            NoFound.setText("check your network connection");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                NewsItem ReadNews = mAdapter.getItem(i);
                String link = ReadNews.getUrl();
                if (link != null) {
                    Uri bookUri = Uri.parse(link);
                    Intent NewsSet = new Intent(Intent.ACTION_VIEW, bookUri);
                    startActivity(NewsSet);
                } else {

                    Toast t = Toast.makeText(MainActivity.this, "no link provided", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });
    }
    @Override
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {
        progressbar.setVisibility(View.VISIBLE);
        Uri main_uri = Uri.parse(REQUEST_URL);
        Uri.Builder URI_builder = main_uri.buildUpon();
        return new AsynClass(this, URI_builder.toString());
    }
    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        progressbar .setVisibility(View.GONE);
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            NoFound.setText("there is no results");
        }
    }
    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {mAdapter.clear();}
    class myAdapter extends ArrayAdapter<NewsItem> {
        public myAdapter(Activity context, ArrayList<NewsItem> touristicalMonuments) {
            super(context ,0 ,touristicalMonuments);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View ListView=convertView;
            if(ListView == null) {
                ListView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            }
            NewsItem List1= (NewsItem) getItem(position);
            TextView SectionName = (TextView) ListView.findViewById(R.id.SectionName);
            SectionName.setText(List1.getSectionName());
            TextView WabTitle= (TextView) ListView.findViewById(R.id.WebTitle);
            WabTitle.setText(List1.getWebTitle());
            TextView Author= (TextView) ListView.findViewById(R.id.Author);
            Author.setText(List1.getAuthor());
            TextView Date= (TextView) ListView.findViewById(R.id.Date);
            Date.setText(List1.getDate());
            return ListView;
        }
    }
}
