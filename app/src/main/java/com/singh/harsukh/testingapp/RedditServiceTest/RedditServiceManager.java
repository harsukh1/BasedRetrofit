package com.singh.harsukh.testingapp.RedditServiceTest;

import com.singh.harsukh.testingapp.Titles;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by harsukh on 3/30/16.
 */
public class RedditServiceManager {

    public static final String BASE_URL = "https://www.reddit.com/";

    private static OkHttpClient buildLogger()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    private static Retrofit retrofit = null;

    public static <S> S createService(String BASE,Class<S> serviceClass )
    {
       Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create());
        if(retrofit == null) {
            retrofit = builder.client(buildLogger()).build();
        }
        return retrofit.create(serviceClass);
    }

    public interface RedditServiceInterface {
        //defines the http method we want to use using retrofit's handy syntax
        @GET(".json")
        Call<Titles> getTitles();
    }
}

