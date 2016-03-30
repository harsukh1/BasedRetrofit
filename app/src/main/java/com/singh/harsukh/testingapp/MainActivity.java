package com.singh.harsukh.testingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.singh.harsukh.testingapp.RedditServiceTest.RedditServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    public final String download_url = "https://www.reddit.com/.json";
    private ArrayList<String> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listview);
        downloadTitles(download_url, listView);
    }

    public void downloadTitles(String url, final ListView l) {
        (new AsyncTask<String, Void, ArrayList>() {
            @Override
            protected ArrayList doInBackground(String... params) {
                ArrayList<String> titles = new ArrayList();
                try {
                    URL url = new URL(params[0]);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream(), "UTF-8"));
                    String json = reader.readLine();
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(json);
                    while (reader.readLine() != null) {
                        json = reader.readLine();
                        buffer.append(json);
                    }
                    JSONObject jsonObject = new JSONObject(buffer.toString());
                    JSONArray array = jsonObject.getJSONObject("data").getJSONArray("children");
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject data = array.getJSONObject(i).getJSONObject("data");
                        String title = data.getString("title");
                        titles.add(title);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                assert titles.size() > 0;
                return titles;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                super.onPostExecute(arrayList);
                onFilledView(arrayList);
                setAdapter(arrayList, l);
            }
        }).execute(url);
    }

    private void setAdapter(ArrayList arrayList, ListView l)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        l.setAdapter(adapter);
    }

    public void onFilledView(ArrayList<String> list)
    {
        Log.d("Download", "");
        for(int i = 0; i< list.size(); ++i)
        {
            mArrayList.add(list.get(i));
            Log.d("onFilledView", mArrayList.get(i));

        }
    }

    public ArrayList<String> getList()
    {
        if(mArrayList==null || mArrayList.isEmpty())
        {
            return null;
        }
        return mArrayList;
    }

    public void callReddit(String BASE_URL)
    {
        RedditServiceManager.RedditServiceInterface service = RedditServiceManager.createService(BASE_URL, RedditServiceManager.RedditServiceInterface.class);
        Call<Titles> call = service.getTitles();
        call.enqueue(new Callback<Titles>() {
            @Override
            public void onResponse(Call<Titles> call, Response<Titles> response) {
                int status_code = response.code();
                Log.e("MainActivity", "" + status_code);
                Titles titles = response.body();
                getTitles(titles);
            }

            @Override
            public void onFailure(Call<Titles> call, Throwable t) {
                Log.e("MainActivity", "Failed Call", t);
            }
        });
    }

    public Titles getTitles(Titles titles)
    {
        return titles;
    }
}
