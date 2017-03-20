package com.nkdroid.tinderswipe;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nkdroid.tinderswipe.RestaurantList.DislikeRestaurantActivity;
import com.nkdroid.tinderswipe.RestaurantList.LikeRestaurantActivity;
import com.nkdroid.tinderswipe.tindercard.FlingCardListener;
import com.nkdroid.tinderswipe.tindercard.SwipeFlingAdapterView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nkdroid.tinderswipe.R.layout.item;


public class MainActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<Data> al = new ArrayList<Data>(); // hold restaurant from yelp
    private SwipeFlingAdapterView flingContainer;

    String term = "restaurant";         // term to search
    double latitude = 33.783784;        // current position
    double longitude = -118.105181;     // current position
    int radius = 3000;                  // radius to search
    int limitSearch = 40;               // limit the result return
    int offset = 0;                     // offset of json object return in array
    int countGroupOffset = 1;           // used to group offset base on limitSearch
    long totalResultFromYelp = 0;       // how many restaurant yelp find out every time of searching
    String status = "";                 // like/dislike

    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseUser mFirebaseUser;
    public static DatabaseReference mDatabase;
    public static String mUserId;

    public final static List<Data> listOfLikedRestaurant = new ArrayList<>();
    public final static List<Data> listOfDislikedRestaurant = new ArrayList<>();


    Button buttonTest;
    Button buttonTest1;

    public static void removeBackground() {
        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //latitude = LocationLoading.latLng.latitude;
        //longitude = LocationLoading.latLng.longitude;

        buttonTest = (Button)findViewById(R.id.button5);

        buttonTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "test button", Toast.LENGTH_LONG).show();
                Intent dislikeIntend = new Intent(MainActivity.this, DislikeRestaurantActivity.class);
                startActivity(dislikeIntend);
            }
        });

        buttonTest1 = (Button)findViewById(R.id.button3);

        buttonTest1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "test button", Toast.LENGTH_LONG).show();
                Intent likeIntend = new Intent(MainActivity.this, LikeRestaurantActivity.class);
                startActivity(likeIntend);
            }
        });

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {

            mUserId = mFirebaseUser.getUid();

            // -----------------------------------------------------------
            // take data from firebase
            mDatabase.child("users").child(mUserId).child("restaurants").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if(dataSnapshot.child("status").getValue().equals("Dislike"))
                        listOfDislikedRestaurant.add(dataSnapshot.getValue(Data.class));
                    else
                        listOfLikedRestaurant.add(dataSnapshot.getValue(Data.class));
                    //Log.d("CREATION",dataSnapshot.child("id").getValue().toString());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //adapter.remove((String) dataSnapshot.child("title").getValue());
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            // -----------------------------------------------------------

            // send asynchonous request to Yelp server
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    Yelp yelp = Yelp.getYelp(MainActivity.this);    // create yelp object


                    // Yelp return a Json String
                    //String businessesList = yelp.search(term, LocationLoading.latLng.latitude,LocationLoading.latLng.longitude, radius, limitSearch, offset); // pass parameter to search method
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

                            JSONObject coordinate = null;
                            String restaurantLatitude = "";
                            String restaurantLongitude = "";
                            if(location.has("coordinate")) {
                                coordinate = (JSONObject) location.get("coordinate");    // get position
                                restaurantLatitude = coordinate.get("latitude").toString();
                                restaurantLongitude = coordinate.get("longitude").toString();
                            }
                            else {
                                restaurantLatitude = "";
                                restaurantLongitude = "";
                            }

                            // add new data restaurant object to array list
                            al.add(new Data(restaurantID,restaurantName, restaurantCatergories, restaurantImage_url,restaurantRating,restaurantPhone,restaurantAddress,restaurantLatitude,restaurantLongitude));

                            //Get more restaurants once we run out
                            if(i == businesses.length() - 1) {
                                //businessesList = yelp.search(term, LocationLoading.latLng.latitude,LocationLoading.latLng.longitude, radius, limitSearch, offset+limitSearch*countGroupOffset); // send another request to yelp with different offset
                                businessesList = yelp.search(term, latitude,longitude, radius, limitSearch, offset); // pass parameter to search method
                                json = new JSONObject(businessesList);
                                JSONArray moreBusinesses = json.getJSONArray("businesses");
                                businesses = moreBusinesses;
                                countGroupOffset++;
                                i = -1;
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String result) {
                }
            }.execute();

            while(al.isEmpty()){}   // wait until the app get a result from yelp




            myAppAdapter = new MyAppAdapter(al, MainActivity.this);
            flingContainer.setAdapter(myAppAdapter);
            flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
                @Override
                public void removeFirstObjectInAdapter() {

                }

                @Override
                public void onLeftCardExit(Object dataObject) {

                    Data saveData = al.get(0);
                    saveData.setStatus("Dislike");
                    al.remove(0);
                    myAppAdapter.notifyDataSetChanged();

                    // add restaurant to realtime database on firebase
                    //mDatabase.child("users").child(mUserId).child("restaurants").push().setValue(saveData);
                    mDatabase.child("users").child(mUserId).child("restaurants").child(saveData.getId()).setValue(saveData);
                }

                @Override
                public void onRightCardExit(Object dataObject) {
                    Data saveData = al.get(0);
                    saveData.setStatus("Like");
                    al.remove(0);
                    myAppAdapter.notifyDataSetChanged();

                    // add restaurant to realtime database on firebase
                    //mDatabase.child("users").child(mUserId).child("restaurants").push().setValue(saveData);
                    mDatabase.child("users").child(mUserId).child("restaurants").child(saveData.getId()).setValue(saveData);
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
                rowView = inflater.inflate(item, parent, false);
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

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            mFirebaseAuth.signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }

}