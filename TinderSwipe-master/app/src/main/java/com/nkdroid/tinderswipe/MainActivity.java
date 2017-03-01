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
    private ArrayList<Data> al = new ArrayList<Data>();
    private SwipeFlingAdapterView flingContainer;

    String term = "restaurant";
    double latitude = 33.80573;
    double longitude = -117.94514;
    int radius = 3000;
    int limitSearch = 40;
    int offset = 0;

    public static void removeBackground() {


        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Yelp yelp = Yelp.getYelp(MainActivity.this);
                String businessesList = yelp.search(term, latitude,longitude, radius, limitSearch, offset);

                try {
                    JSONObject json = new JSONObject(businessesList);
                    JSONArray businesses = json.getJSONArray("businesses");

                    for(int i = 0; i< businesses.length();i++){
                        JSONObject restaurant = (JSONObject) businesses.get(i);
                        String restaurantID = restaurant.get("id").toString();
                        String restaurantName = restaurant.get("name").toString();

                        String restaurantCatergories = null;
                        JSONArray catergoriesJsonObject = (JSONArray)restaurant.get("categories");
                        List subList = new ArrayList();
                        for(int j =0;j<catergoriesJsonObject.length();j++){
                            JSONArray obj = (JSONArray)catergoriesJsonObject.get(j);
                            subList.add(obj.get(0).toString());
                        }
                        restaurantCatergories = TextUtils.join(", ", subList);

                        String restaurantImage_url = "";
                        if(restaurant.has("image_url")){
                            restaurantImage_url = restaurant.get("image_url").toString();
                            restaurantImage_url = restaurantImage_url.substring(0, restaurantImage_url.length()-6) + "l.jpg";
                        }
                        else
                            restaurantImage_url = " ";

                        String restaurantRating = restaurant.get("rating").toString();
                        String restaurantPhone = restaurant.get("phone").toString();
                        restaurantPhone = restaurantPhone.substring(0,3) + "-" + restaurantPhone.substring(3,6) + "-" + restaurantPhone.substring(6, restaurantPhone.length());

                        JSONObject location = (JSONObject) restaurant.get("location");
                        JSONArray addressList =  location.getJSONArray("display_address");
                        String restaurantAddress = addressList.get(0) + ", " + addressList.get(1);

                        JSONObject coordinate = (JSONObject) location.get("coordinate");
                        String restaurantLatitude = coordinate.get("latitude").toString();
                        String restaurantLongitude = coordinate.get("longitude").toString();

                        al.add(new Data(restaurantID,restaurantName, restaurantCatergories, restaurantImage_url,restaurantRating,restaurantPhone,restaurantAddress,restaurantLatitude,restaurantLongitude));
                    }
                }
                catch (Exception e){
                    System.out.print(e.getStackTrace());
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
            }
        }.execute();

        while(al.isEmpty()){}

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

            Data currentRestaurantOnScreen = parkingList.get(position);

            String display = "Name: " + currentRestaurantOnScreen.getName() + "\n" +
                                "Category: " + currentRestaurantOnScreen.getCategories() + "\n" +
                                "Rating: " + currentRestaurantOnScreen.getRating() + "/5"+ "\n" +
                                "Phone: " + currentRestaurantOnScreen.getPhone() + "\n" +
                                "Address: " + currentRestaurantOnScreen.getAddress();

            viewHolder.DataText.setText(display);

            Glide.with(MainActivity.this).load(parkingList.get(position).getImage_url()).into(viewHolder.cardImage);

            return rowView;
        }
    }
}
