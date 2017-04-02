package com.nkdroid.tinderswipe.RestaurantList;

import android.content.Intent;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nkdroid.tinderswipe.Data;
import com.nkdroid.tinderswipe.MainActivity;
import com.nkdroid.tinderswipe.R;

import java.io.Serializable;

import static com.nkdroid.tinderswipe.MainActivity.mDatabase;
import static com.nkdroid.tinderswipe.MainActivity.mUserId;
import static com.nkdroid.tinderswipe.R.id.gridView;

public class LikeRestaurantActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_restaurant);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, MainActivity.listOfLikedRestaurant);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Data item = (Data) parent.getItemAtPosition(position);

                //Create intent
                Intent intent = new Intent(LikeRestaurantActivity.this, DetailsActivity.class).putExtra("From","LikeRestaurantActivity");
                intent.putExtra("myData", item);
                finish();
                //Start details activity
                startActivity(intent);
            }
        });

    }
}
