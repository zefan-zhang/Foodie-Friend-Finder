package edu.neu.foodiefriend;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.neu.foodiefriend.models.YelpDataClass;
import edu.neu.foodiefriend.models.YelpRestaurant;
import edu.neu.foodiefriend.yelpData.YelpService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static edu.neu.foodiefriend.LoginActivity.loginUser;

public class SearchActivity extends AppCompatActivity {

    private Spinner cuisineDropDown;
    private Spinner distanceDropDown;
    private Spinner priceDropDown;

    private RestaurantsAdapter adapter;
    private static final String BASE_URL = "https://api.yelp.com/v3/";
    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String TAG = "searchActivity";
    private static final int LOCATION_REQUEST_CODE = 101;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private double latitude;
    private double longitude;

    private ArrayList<YelpRestaurant> selectedRestaurants = new ArrayList<YelpRestaurant>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // cuisine select
        cuisineDropDown = findViewById(R.id.cusineSpinner);
        ArrayAdapter<String> cusineAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.cuisineInSearch));

        cusineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisineDropDown.setAdapter(cusineAdapter);


        //Radius selection
        distanceDropDown = findViewById(R.id.distanceSpinner);
        ArrayAdapter<String> radiusAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.distanceOptions));

        radiusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceDropDown.setAdapter(radiusAdapter);

        // price select
        priceDropDown = findViewById(R.id.priceSpinner);
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.priceOptions));

        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceDropDown.setAdapter(priceAdapter);

        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Yelp Params
                String cuisine = cuisineDropDown.getSelectedItem().toString();
                int meter = getDistance();
                String priceRange = getPriceRange();
                // on each click, call the function to get the current latitude and longitude.
//                LatLon();
                LatLonOld();
                recyclerSetup();

                ArrayList<YelpRestaurant> restaurants = new ArrayList<YelpRestaurant>();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                YelpService yelpService = retrofit.create(YelpService.class);
                yelpService.searchRestaurants(API_KEY, cuisine, latitude,
                        longitude, meter, priceRange).enqueue(new Callback<YelpDataClass>() {


                    @Override
                    public void onResponse(Call<YelpDataClass> call, Response<YelpDataClass> response) {
                        int statusCode = response.code();
                        YelpDataClass yelpDataClass = response.body();
                        if (yelpDataClass == null) {
                            Log.w(TAG, "Did not receive valid response from Yelp API");
                            return;
                        }
                        restaurants.addAll(yelpDataClass.restaurants);
                        adapter.setRestaurants(restaurants);

                    }

                    @Override
                    public void onFailure(Call<YelpDataClass> call, Throwable t) {
                        // Log error here since request failed

                    }
                });

            }
        });

        // go matched foodies activity
        Button go_match = findViewById(R.id.match_foodie);
        go_match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSelected()) {
                    updateUserSelectedResr();
                    Intent foodieResultActivity = new Intent(SearchActivity.this, FoodieResultActivity.class);
                    startActivity(foodieResultActivity);
                }
            }
        });
    }

    private void recyclerSetup() {
        RecyclerView recyclerView;

        adapter = new RestaurantsAdapter(R.layout.item_restaurant, SearchActivity.this);
        recyclerView = findViewById(R.id.rvRestaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RestaurantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                YelpRestaurant selectedRes = adapter.getRestaurantList().get(position);
                if (!selectedRestaurants.contains(selectedRes)) {
                    selectedRestaurants.add(selectedRes);
                } else {
                    selectedRestaurants.remove(selectedRes);
                }
                if (selectedRestaurants.size() > 3) {
                    Toast.makeText(SearchActivity.this, "Please select no more than 3 restaurants", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public String getPriceRange() {
        String price = priceDropDown.getSelectedItem().toString();
        if (price.equals("Any Range")) {
            return "1, 2, 3, 4";
        } else if (price.equals("$")) {
            return "1";
        } else if (price.equals("$$")) {
            return "2";
        } else if (price.equals("$$$")) {
            return "3";
        } else {
            return "4";
        }
    }

    //get use selected radius and covert to meters
    //The max value of YELP can take which is 40000 meters (about 25 miles)
    public int getDistance() {
        double mi = 0;
        String miles = distanceDropDown.getSelectedItem().toString();
        if (miles.equals("Any distance")) {
            return 40000;
        } else if (miles.equals("0.3 mi")) {
            mi = 0.3;
        } else if (miles.equals("1 mi")) {
            mi = 1;
        } else if (miles.equals("5 mi")) {
            mi = 5;
        } else if (miles.equals("20 mi")) {
            mi = 20;
        }

        return (int) Math.round(mi * 1609.34);

    }

    private void LatLon() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            getCurrentLocation();
        }

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            Toast.makeText(this, "No Access to GPS", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean isGPSOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSOn) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    String coordinates = "Latitude: " + location.getLatitude() + "\n" +
                            "Longitude: " + location.getLongitude();

                    Toast.makeText(SearchActivity.this, coordinates, Toast.LENGTH_LONG).show();

                    locationManager.removeUpdates(locationListener);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            };
        }

        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            assert lm != null;
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationManager.removeUpdates(locationListener);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });
        }
    }

    private void LatLonOld() {
        if (checkPermissions()) {
            LatitudeAndLongitude latitudeAndLongitude = new LatitudeAndLongitude(getApplicationContext());
            Location location = latitudeAndLongitude.getLocation();

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
    }

    private boolean checkPermissions() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else {
            requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
            return false;
        }
    }

    protected void requestPermissions(String permissionType, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestCode);
    }

    public boolean isValidSelected() {
        if (selectedRestaurants.size() == 0) {
            Toast.makeText(this, "Please selected at least 1 restaurant", Toast.LENGTH_SHORT).show();
            return false;
        } else if (selectedRestaurants.size() > 3) {
            Toast.makeText(this, "Please selected no more than 3 restaurants", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void updateUserSelectedResr() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
        List<String> restaurantNames = new ArrayList<String>();
        for (YelpRestaurant restaurant : selectedRestaurants) {
            restaurantNames.add(restaurant.getRestaurantName());
        }
        loginUser.setInterestedRestaurants(restaurantNames);
        userRef.child("Users").child(loginUser.getUserId()).child("interestedRestaurants").setValue(restaurantNames);
    }
}
