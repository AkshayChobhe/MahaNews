package com.example.mahanews;

import android.content.Context;
import android.widget.Toast;

import com.example.mahanews.Models.NewsApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    //api address added
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public void getsNewsHeadlines(OnFetchDataListener listener, String category, String query){
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);

        //getting api added - country can change
        Call<NewsApiResponse> call = callNewsApi.callHeadlines("us", category, query, context.getString(R.string.api_key));

        try{
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(Call<NewsApiResponse> call, Response<NewsApiResponse> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context, "Error!!", Toast.LENGTH_SHORT).show();
                    }
                   //getting response here when successful
                    listener.onFetchData(response.body().getArticles(), response.message());
                }

                //if request failed
                @Override
                public void onFailure(Call<NewsApiResponse> call, Throwable t) {
                    //when response failed
                    listener.onError("Request Failed!");
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public RequestManager(Context context) {
        this.context = context;
    }

    //To access api queries
    public interface CallNewsApi{
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String api_key
        );
    }
}
