package edu.neu.foodiefriendfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import edu.neu.foodiefriendfinder.models.User;

import static edu.neu.foodiefriendfinder.LoginActivity.loginUser;

public class FoodieResultActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private int searchButtonCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_result);

        final List<User> users = new ArrayList<>();

        final List<User> matchFoodies = new ArrayList<>();

        final HashSet<User> uniqueListOfFoodies = new HashSet<>();

        reference = FirebaseDatabase.getInstance().getReference();

        TextView textView = findViewById(R.id.testShow);

        Button button = findViewById(R.id.searchFoodie);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                        System.out.println("adding user" + user);
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

    private boolean isNoMatches(List<User> foodieMatches) {
        if (foodieMatches.isEmpty()) {
            Toast.makeText(this, "Sorry no matches", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}