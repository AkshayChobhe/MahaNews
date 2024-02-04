package com.example.mahanews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mahanews.Models.NewsApiResponse;
import com.example.mahanews.Models.NewsHeadlines;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Exclude title,status and action bar from fading
//        Fade fade = new Fade();
//        View decor = getWindow().getDecorView();
//        fade.excludeTarget(decor.findViewById(androidx.fragment.R.id.action_container),true);
//        fade.excludeTarget(android.R.id.statusBarBackground,true);
//        fade.excludeTarget(android.R.id.navigationBarBackground,true);
////        getWindow().setEnterTransition(fade);
//        getWindow().setExitTransition(fade);


        searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of " + query);
                dialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                //by default general category selected
                manager.getsNewsHeadlines(listener,"general", query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching news articles..");
        dialog.show();

        //buttons
        b1 = findViewById(R.id.btn_1);
        b1.setOnClickListener(this);
        b2 = findViewById(R.id.btn_2);
        b2.setOnClickListener(this);
        b3 = findViewById(R.id.btn_3);
        b3.setOnClickListener(this);
        b4 = findViewById(R.id.btn_4);
        b4.setOnClickListener(this);
        b5 = findViewById(R.id.btn_5);
        b5.setOnClickListener(this);
        b6 = findViewById(R.id.btn_6);
        b6.setOnClickListener(this);
        b7 = findViewById(R.id.btn_7);
        b7.setOnClickListener(this);


        RequestManager manager = new RequestManager(this);
        manager.getsNewsHeadlines(listener,"general", null);
    }

    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>(){
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message)
        {
            if(list.isEmpty()){ //if api fails to run
                Toast.makeText(MainActivity.this, "No Data Found!!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else{
                showNews(list);
                dialog.dismiss();
            }

        }
        //if api return error
        @Override
        public void onError(String message){
            Toast.makeText(MainActivity.this, "An error Occured!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        Intent i = new Intent(MainActivity.this,DetailsActivity.class)
                .putExtra("data",headlines);
//        for transition
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
//        for image transition
        final ImageView imageView = findViewById(R.id.img_headline);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView, ViewCompat.getTransitionName(imageView));
//        startActivity(i,options.toBundle());
        startActivity(i,b);
    }


    @Override
    public void onClick(View v) {
        //onclick listener on category selection
        Button button = (Button) v;
        String category = button.getText().toString();
        //dialog
        dialog.setTitle("Fetching news articles of " + category + "..");
        dialog.show();
        RequestManager manager = new RequestManager(this);
        manager.getsNewsHeadlines(listener,category, null);
    }
}