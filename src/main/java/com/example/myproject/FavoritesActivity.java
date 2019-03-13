package com.example.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.example.myproject.adapter.FavoritesAdapter;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView favorites;
    FavoritesAdapter favoritesAdapter;
    ArrayList favoriteslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favorites = findViewById(R.id.favorites);
        favoriteslist = new ArrayList();
        for (int i = 0; i < 10; i++) {
            favoriteslist.add(i+"");
        }
        favoritesAdapter = new FavoritesAdapter(R.layout.layout_favorites,favoriteslist);
        favorites.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL,false));
        favorites.setAdapter(favoritesAdapter);

    }
}
