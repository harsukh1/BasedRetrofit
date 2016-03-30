package com.singh.harsukh.testingapp;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import junit.framework.TestResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by harsukh on 3/27/16.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private ListView listView;
    private boolean set_up = false;
    //always start with constructor matching super
    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        if(set_up) {
            return;
        }
        super.setUp();
        mActivity = getActivity();
        listView = (ListView) mActivity.findViewById(R.id.listview);
        url = "https://www.reddit.com/.json";
        set_up = true;
    }

    public void testActivity()
    {
        assertNotNull(mActivity);
    }

    public void testList()
    {
        assertNotNull(listView);
    }

    private String url;

    public void testConnection()
    {
        String s_url = mActivity.download_url;
        assertEquals(s_url, url);
        try
        {
            URL url = new URL(s_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            assertEquals(HttpURLConnection.HTTP_OK, conn.getResponseCode());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void testListView()
    {
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mActivity.downloadTitles(url, listView);
            }
        });
        while(mActivity.getList() == null)
        {
            synchronized (this) {
                try {
                    this.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        int arraySize = mActivity.getList().size();
        for (int i = 0; i < arraySize; ++i) {
            assertEquals(mActivity.getList().get(i), listView.getItemAtPosition(i).toString());
        }
    }
}
