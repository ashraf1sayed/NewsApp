package com.example.ashraf.newsapp;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
public class Quest {
    static HttpURLConnection urlConnection = null;
    private static final String log_cat = Quest.class.getSimpleName();
    public Quest() {}
    public static List<NewsItem> fetch(String request) {
        URL url = converter(request);
        InputStream inputs=null;
        String jsonResponse = null;
        try {
            inputs = makeHttpRequest(url);
            jsonResponse = readFromStream(inputs);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputs != null) {
                inputs.close();
            }
            if (inputs != null) {
                inputs.close();
            }
        } catch (IOException e) {
            Log.e(log_cat, "Problem making the HTTP request.", e);
        }
        List<NewsItem> News = extractFeatureFromJson(jsonResponse);
        return News;
    }
    public static URL converter(String ul) {
        URL url = null;
        try {
            url = new URL(ul);
        } catch (MalformedURLException e) {
            Log.e(log_cat, "there is a problem in converting url", e);
        }
        return url;
    }
    public static InputStream makeHttpRequest(URL url) throws IOException {
        InputStream inputStream = null;
        if (url == null) {
            return inputStream;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
            } else {
                Log.e(log_cat, "Error in connection: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(log_cat, "there is a problem in getting json.", e);
        }
        return inputStream;
    }
    private static  String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<NewsItem> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<NewsItem> newsList = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(newsJSON);
            JSONObject response = baseJsonObject.getJSONObject("response");
            JSONArray result = response.getJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject newsObject = result.getJSONObject(i);
                String title = newsObject.getString("webTitle");
                String webUrl = newsObject.getString("webUrl");
                String section = newsObject.getString("sectionName");
                String date = newsObject.getString("webPublicationDate");
                String author = "N/A";
                String[] authorsArray = new String[]{};
                List<String> authorsList = new ArrayList<>();
                JSONArray tagsArray = newsObject.getJSONArray("tags");
                for (int j = 0; j < tagsArray.length(); j++) {
                    JSONObject tagsObject = tagsArray.getJSONObject(j);
                    String firstName = tagsObject.optString("firstName");
                    String lastName = tagsObject.optString("lastName");
                    String authorName;
                    if (TextUtils.isEmpty(firstName)) {
                        authorName = lastName;
                    } else {
                        authorName = firstName + " " + lastName;
                    }
                    authorsList.add(authorName);
                }
                if (authorsList.size() == 0) {
                    author = "N/A";
                } else {
                    author = TextUtils.join(", ", authorsList);
                }
                NewsItem news = new NewsItem(author,title,section,date,webUrl);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("Quest", "Problem parsing the news JSON results", e);
        }
        return newsList;
    }
}
