package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import edu.neu.foodiefriendfinder.models.User;

import static edu.neu.foodiefriendfinder.LoginActivity.loginUser;

public class FoodieResultActivity extends AppCompatActivity {

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodie_result);

        final List<User> users = new ArrayList<User>();

        userRef = FirebaseDatabase.getInstance().getReference();

        loginUser;

        TextView textView = findViewById(R.id.textView5);

        Button button = findViewById(R.id.searchFoodie);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
