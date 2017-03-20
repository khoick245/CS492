package com.nkdroid.tinderswipe.DislikeRestaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nkdroid.tinderswipe.Data;
import com.nkdroid.tinderswipe.MainActivity;
import com.nkdroid.tinderswipe.R;

import java.util.ArrayList;
import java.util.List;

import static com.nkdroid.tinderswipe.LocationLoading.i;

public class DislikeRestaurantActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dislike_restaurant);

        for(int count =0;count<MainActivity.listOfDislikedRestaurant.size();count++)
            Log.d("CREATION", MainActivity.listOfDislikedRestaurant.get(count).getName());
    }
}
