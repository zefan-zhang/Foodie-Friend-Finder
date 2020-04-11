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
import java.util.List;

import edu.neu.foodiefriendfinder.models.User;

import static edu.neu.foodiefriendfinder.LoginActivity.loginUser;

public class FoodieResultActivity extends AppCompatActivity {

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_result);

        final List<User> users = new ArrayList<User>();

        final List<User> matchFoodies = new ArrayList<User>();

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
                                        matchFoodies.add(user);
                                    }
                                }
                            }
                        }
                        isNoMatches(matchFoodies);
//                        String matchfoodiesName = "";
//                        for (User user : matchFoodies) {
//                            matchfoodiesName += "  " + user.getFirstName();
//                        }
                        isMatchExistsAlready(matchFoodies, textView);
//                        textView.setText(matchfoodiesName);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void isNoMatches(List<User> foodieMatches) {
        if (foodieMatches.isEmpty()) {
            Toast.makeText(this, "Sorry no matches", Toast.LENGTH_SHORT).show();
        }
    }

    private void isMatchExistsAlready(List<User> foodieMatches, TextView textView) {
        String matchfoodiesName = "";
        int initialSize = foodieMatches.size();
        System.out.println("Initial size: " + initialSize);
        for (User match : foodieMatches) {
//            if (foodieMatches.contains(match)) {
            if (initialSize == foodieMatches.size()) {
                matchfoodiesName += "  " + match.getFirstName();
            }
            else {
                Toast.makeText(this, "This user: " + match.getFirstName() + " is already accounted for!",
                        Toast.LENGTH_SHORT).show();
            }
        }
        textView.setText(matchfoodiesName);
    }
}