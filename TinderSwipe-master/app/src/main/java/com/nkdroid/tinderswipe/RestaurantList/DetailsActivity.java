package com.nkdroid.tinderswipe.RestaurantList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nkdroid.tinderswipe.Data;
import com.nkdroid.tinderswipe.MainActivity;
import com.nkdroid.tinderswipe.R;

import java.util.List;

import static com.nkdroid.tinderswipe.MainActivity.listOfDislikedRestaurant;
import static com.nkdroid.tinderswipe.MainActivity.listOfLikedRestaurant;
import static com.nkdroid.tinderswipe.MainActivity.mDatabase;
import static com.nkdroid.tinderswipe.MainActivity.mFirebaseAuth;
import static com.nkdroid.tinderswipe.MainActivity.mFirebaseUser;
import static com.nkdroid.tinderswipe.MainActivity.mUserId;
import static com.nkdroid.tinderswipe.MainActivity.viewHolder;
import static com.nkdroid.tinderswipe.R.layout.item;

public class DetailsActivity extends AppCompatActivity {

    ImageButton deleteButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final Data item = getIntent().getParcelableExtra("myData");

        String display = "Name: " + item.getName() + "\n" +
                "Category: " + item.getCategories() + "\n" +
                "Rating: " + item.getRating() + "/5"+ "\n" +
                "Phone: " + item.getPhone() + "\n" +
                "Address: " + item.getAddress();

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(display);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        //imageView.setImageBitmap(bitmap);
        Glide.with(DetailsActivity.this).load(item.getImage_url()).into(imageView);


        deleteButton = (ImageButton)findViewById(R.id.imageButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "test button", Toast.LENGTH_LONG).show();

                mDatabase.child("users").child(mUserId).child("restaurants").child(item.getId()).removeValue();
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_LONG).show();

                if(getIntent().getStringExtra("From").equals("LikeRestaurantActivity")) {
                    removeItemFromListRestaurant(item,MainActivity.listOfLikedRestaurant);
                    Intent previous = new Intent(DetailsActivity.this, LikeRestaurantActivity.class);
                    finish();
                    startActivity(previous);
                }
                else{
                    removeItemFromListRestaurant(item,MainActivity.listOfDislikedRestaurant);
                    Intent previous = new Intent(DetailsActivity.this, DislikeRestaurantActivity.class);
                    finish();
                    startActivity(previous);
                }
            }
        });
    }

    public void removeItemFromListRestaurant(Data item, List<Data> listRestaurant){
        for(int index = 0;index < listRestaurant.size();index++) {
            if (listRestaurant.get(index).getId().equals(item.getId())){
                listRestaurant.remove(index);
                break;
            }
        }
    }
}
