package edu.neu.foodiefriendfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.List;

import edu.neu.foodiefriendfinder.models.User;

import static edu.neu.foodiefriendfinder.LoginActivity.loginUser;

public class FoodieResultActivity extends AppCompatActivity {

    private DatabaseReference reference;

    private FoodiesAdapter adapter;

    private ArrayList<User> selectedFoodies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_foodie_result);

        final List<User> users = new ArrayList<>();

        final List<User> matchFoodies = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();

        TextView textView = findViewById(R.id.testShow);

        Button searchButton = findViewById(R.id.searchFoodie);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerSetup();

                reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                        for (DataSnapshot child : children) {
                            User user = child.getValue(User.class);
                            users.add(user);
                        }
                        for (User user : users) {
                            for (String restaurant : loginUser.getInterestedRestaurants()) {
                                if (user.getInterestedRestaurants().contains(restaurant)
                                && (!user.getUserId().equals(loginUser.getUserId()))) {
                                    if (!matchFoodies.contains(user)) {
                                        matchFoodies.add(user);
                                    }
                                }
                            }
                        }
                        if (!isNoMatches(matchFoodies)) {
                            StringBuilder foodieName = new StringBuilder();
                            for (User foodie : matchFoodies) {
                                foodieName.append("  ").append(foodie.getFirstName());
                            }
                            adapter.setFoodies(matchFoodies);
                            textView.setText(foodieName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void recyclerSetup() {
        RecyclerView recyclerView;

        adapter = new FoodiesAdapter(R.layout.item_foodie, FoodieResultActivity.this);
        recyclerView = findViewById(R.id.rvFoodie);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodieResultActivity.this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FoodiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                User selectedFoodie = adapter.getFoodieList().get(position);
                if (!selectedFoodies.contains(selectedFoodie) && selectedFoodies.size() < 1) {
                    selectedFoodies.add(selectedFoodie);
                    System.out.println("selected foodie: " + selectedFoodies);
                    confirmDineWithFoodie(selectedFoodie);
                } else {
                    Toast.makeText(FoodieResultActivity.this, "Please select no more than 1 Foodie to dine with!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNoMatches(List<User> foodieMatches) {
        if (foodieMatches.isEmpty()) {
            Toast.makeText(this, "Sorry no matches", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void confirmDineWithFoodie(User foodie) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_DayNight_Dialog));

        alert.setTitle("Confirm Foodie");
        StringBuilder fullName = new StringBuilder(foodie.getFirstName() + " " + foodie.getLastName());
        alert.setMessage("Dine with " + fullName + "?");
        alert.setCancelable(true);

        String yesOption = "Yes";
        alert.setPositiveButton(yesOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO -- Send selected foodie to firebase
            }
        });

        String cancelOption = "No";
        alert.setNegativeButton(cancelOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFoodies.remove(foodie);
                dialog.cancel();
            }
        });
        AlertDialog alert1 = alert.create();
        alert1.show();
    }
}