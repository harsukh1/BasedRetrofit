package com.singh.harsukh.testingapp;

import android.util.Log;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.singh.harsukh.testingapp.RedditServiceTest.RedditServiceManager;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.internal.Shadow;
import org.robolectric.shadows.httpclient.FakeHttp;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by harsukh on 3/29/16.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class NetworkResponseTest {

    RedditServiceManager.RedditServiceInterface service;
    @Before
    public void setUp()
    {
        //FakeHttp.setDefaultHttpResponse(200, "OK");
        //activity = Robolectric.buildActivity(MainActivity.class).create().get();
      service = RedditServiceManager.createService("http://localhost:8089/"
                , RedditServiceManager.RedditServiceInterface.class);

        assertNotNull(service);
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(false);
    }

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void testResponse()  {
        stubFor(get(urlMatching(".*"))
                .atPriority(5)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("reddit_response.json")));
        Call<Titles> call = service.getTitles();
        assertNotNull(call);
        Response<Titles> response;
        try {
            response = call.execute();
            Titles titles = response.body();
        } catch (IOException e) {
            Log.e("NetworkResponseTest", "Call not successful", e);
        }


//        Result result = myHttpServiceCallingObject.doSomething();
//
//        assertTrue(result.wasSuccessFul());
//
//        verify(postRequestedFor(urlMatching("/my/resource/[a-z0-9]+"))
//                .withRequestBody(matching(".*<message>1234</message>.*"))
//                .withHeader("Content-Type", notMatching("application/json")));

    }
}