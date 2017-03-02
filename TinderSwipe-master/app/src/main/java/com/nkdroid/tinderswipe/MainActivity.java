package com.nkdroid.tinderswipe;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nkdroid.tinderswipe.tindercard.FlingCardListener;
import com.nkdroid.tinderswipe.tindercard.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<Data> al = new ArrayList<Data>(); // hold restaurant from yelp
    private SwipeFlingAdapterView flingContainer;

    String term = "restaurant";     // term to search
    double latitude = 33.783784;     // current position
    double longitude = -118.105181;  // current position
    int radius = 3000;              // radius to search
    int limitSearch = 40;           // limit the result return
    int offset = 0;                 // offset of json object return in array
    int countGroupOffset = 1;       // used to group offset base on limitSearch
    long totalResultFromYelp = 0;    // how many restaurant yelp find out every time of searching

    public static void removeBackground() {


        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        //String s1="";
        //String s2="";

        // send asynchonous request to Yelp server
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Yelp yelp = Yelp.getYelp(MainActivity.this);    // create yelp object

                // Yelp return a Json String
                String businessesList = yelp.search(term, latitude,longitude, radius, limitSearch, offset); // pass parameter to search method

                try {
                    JSONObject json = new JSONObject(businessesList);           // parse string to json
                    JSONArray businesses = json.getJSONArray("businesses");     // get all restaurants based on key "businesses", return a jsonarray

                    totalResultFromYelp = (long)json.getLong("total");  // get total result

                    // loop through every single json object in array
                    for(int i = 0; i< businesses.length();i++){
                        
                        JSONObject restaurant = (JSONObject) businesses.get(i); // get json object in jsonarray based on index
                        String restaurantID = restaurant.get("id").toString();  // get restaurant id based on the key "id"
                        String restaurantName = restaurant.get("name").toString(); // get restaurant id based on the key "name"

                        String restaurantCatergories = null;
                        if(restaurant.has("categories")) {
                            JSONArray catergoriesJsonObject = (JSONArray) restaurant.get("categories");  // take jsonarray of catergories
                            List subList = new ArrayList();
                            for (int j = 0; j < catergoriesJsonObject.length(); j++) {
                                JSONArray obj = (JSONArray) catergoriesJsonObject.get(j); // get every single element in json array
                                subList.add(obj.get(0).toString()); // get first element of a json catergory, put it into list
                            }
                            restaurantCatergories = TextUtils.join(", ", subList); // change the list to string form
                        }
                        else
                        restaurantCatergories = " ";

                        String restaurantImage_url = "";
                        if(restaurant.has("image_url")){  // get image url
                            restaurantImage_url = restaurant.get("image_url").toString();
                            restaurantImage_url = restaurantImage_url.substring(0, restaurantImage_url.length()-6) + "l.jpg"; // change the name of image to get large image
                        }
                        else
                            restaurantImage_url = " ";

                        String restaurantRating = "";
                        if(restaurant.has("rating"))
                            restaurantRating = restaurant.get("rating").toString();  // get rating
                        else
                            restaurantRating = " ";

                        String restaurantPhone = "";
                        if(restaurant.has("phone")) {
                            restaurantPhone = restaurant.get("phone").toString();    // get phone
                            restaurantPhone = restaurantPhone.substring(0, 3) + "-" + restaurantPhone.substring(3, 6) + "-" + restaurantPhone.substring(6, restaurantPhone.length()); // format the phone
                        }
                        else
                            restaurantPhone = " ";

                        JSONObject location = (JSONObject) restaurant.get("location"); // get location
                        JSONArray addressList =  location.getJSONArray("display_address"); // get display address in location
                        String restaurantAddress = "";
                        for(int k=0;k<addressList.length();k++)  // get display address as string
                            if(k==addressList.length()-1)
                                restaurantAddress += addressList.get(k);
                            else
                                restaurantAddress += addressList.get(k) + ", ";

                        JSONObject coordinate = (JSONObject) location.get("coordinate");    // get position
                        String restaurantLatitude = coordinate.get("latitude").toString();
                        String restaurantLongitude = coordinate.get("longitude").toString();

                        // add new data restaurant object to array list
                        al.add(new Data(restaurantID,restaurantName, restaurantCatergories, restaurantImage_url,restaurantRating,restaurantPhone,restaurantAddress,restaurantLatitude,restaurantLongitude));

                        //Get more restaurants once we run out
                        if(i == businesses.length() - 1) {
                            businessesList = yelp.search(term, latitude,longitude, radius, limitSearch, offset+limitSearch*countGroupOffset); // send another request to yelp with different offset
                            json = new JSONObject(businessesList);
                            JSONArray moreBusinesses = json.getJSONArray("businesses");
                            businesses = moreBusinesses;
                            countGroupOffset++;
                            i = -1;
                        }
                    }
                }
                catch (Exception e){
                    System.out.print(e.getStackTrace());
                    String s3="";
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
            }
        }.execute();

        while(al.isEmpty()){}   // while until get a result from yelp

        myAppAdapter = new MyAppAdapter(al, MainActivity.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

            }

            @Override
            public void onRightCardExit(Object dataObject) {

                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                myAppAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;


    }

    public class MyAppAdapter extends BaseAdapter {


        public List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Data currentRestaurantOnScreen = parkingList.get(position); // get current element in arraylist

            // create display string
            String display = "Name: " + currentRestaurantOnScreen.getName() + "\n" +
                                "Category: " + currentRestaurantOnScreen.getCategories() + "\n" +
                                "Rating: " + currentRestaurantOnScreen.getRating() + "/5"+ "\n" +
                                "Phone: " + currentRestaurantOnScreen.getPhone() + "\n" +
                                "Address: " + currentRestaurantOnScreen.getAddress();

            viewHolder.DataText.setText(display);

            // display image
            Glide.with(MainActivity.this).load(parkingList.get(position).getImage_url()).into(viewHolder.cardImage);

            return rowView;
        }
    }
}
