package com.nkdroid.tinderswipe.RestaurantList;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nkdroid.tinderswipe.Data;
import com.nkdroid.tinderswipe.MainActivity;
import com.nkdroid.tinderswipe.R;

public class DislikeRestaurantActivity extends AppCompatActivity {


    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dislike_restaurant);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, MainActivity.listOfDislikedRestaurant);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Data item = (Data) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(DislikeRestaurantActivity.this, DetailsActivity.class);
                intent.putExtra("myData", item);

                //Start details activity
                startActivity(intent);
            }
        });
    }
}
