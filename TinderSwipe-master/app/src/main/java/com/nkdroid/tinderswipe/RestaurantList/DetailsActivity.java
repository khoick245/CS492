package com.nkdroid.tinderswipe.RestaurantList;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkdroid.tinderswipe.Data;
import com.nkdroid.tinderswipe.MainActivity;
import com.nkdroid.tinderswipe.R;

import static com.nkdroid.tinderswipe.MainActivity.viewHolder;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Data item = getIntent().getParcelableExtra("myData");

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
    }
}
