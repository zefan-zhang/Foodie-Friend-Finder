package edu.neu.foodiefriend;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.neu.foodiefriend.models.User;

import static edu.neu.foodiefriend.LoginActivity.loginUser;

public class FoodieResultActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FoodiesAdapter adapter;
    private ArrayList<User> selectedFoodies = new ArrayList<>();
    private List<String> allLanguages = new ArrayList<>();
    private List<String> selectedFoodieLanguages = new ArrayList<>();

    private String[] languagesBank;
    private boolean[] checkedLanguages;
    private ArrayList<Integer> languagesIndex = new ArrayList<>();
    private ArrayList<String> userLanguages = new ArrayList<>();
    private TextView languagesSelectedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_foodie_result);

        final List<User> users = new ArrayList<>();
        final List<User> testUsers = new ArrayList<>();
        final List<User> matchFoodies = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();

        Switch statusToggle = findViewById(R.id.statusToggle);

        /*
        Get the data from fire base first, then process it for all languages.
         */
        reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    User user = child.getValue(User.class);
                    testUsers.add(user);
                }
                languagesBank = getAllFoodieLanguages(testUsers);
                checkedLanguages = new boolean[languagesBank.length];
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ImageButton languageSelectButton = findViewById(R.id.languageFoodieSelectButton);
        languagesSelectedText = findViewById(R.id.languagesFoodieSelected);
        languageSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSelectDialog(languagesBank, checkedLanguages, languagesIndex, userLanguages, languagesSelectedText).show();
            }
        });
        
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
                            List<User> filteredFoodies = new ArrayList<>();

//                            if (filteredFoodies.isEmpty()) {
//                                // Display the total. This is the default action.
//                                adapter.setFoodies(matchFoodies);
//                            }

                            for (User foodie : matchFoodies) {
                                for (String language : selectedFoodieLanguages) {
                                    if (foodie.getLanguages().contains(language)) {
                                        filteredFoodies.add(foodie);
                                    }
                                    else if (!foodie.getLanguages().contains(language)) {
                                        filteredFoodies.remove(foodie);
                                    }
                                }
                            }

//                            if (!filteredFoodies.isEmpty()) {
//                                // Display foodies based on filters. Non default action.
//                                matchFoodies.clear();
//                                adapter.setFoodies(filteredFoodies);
//                            }
                            adapter.setFoodies(filteredFoodies);
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

        adapter.setDineWithButtonListener(new FoodiesAdapter.OnDineWithButtonItemClickListener() {
            @Override
            public void onDineWithIsClick(View button, int position) {
                User selectedFoodie = adapter.getFoodieList().get(position);
                if (!selectedFoodies.contains(selectedFoodie) && selectedFoodies.size() < 1) {
                    selectedFoodies.add(selectedFoodie);
                    confirmDineWithFoodie(selectedFoodie);
                }
            }
        });
        recyclerView.setAdapter(adapter);
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
        String userId = foodie.getUserId();
        alert.setMessage("Dine with " + foodie.getFirstName() + " " + foodie.getLastName() + "?");
        alert.setCancelable(true);

        String yesOption = "Yes";
        alert.setPositiveButton(yesOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setSelectedFoodie(userId);
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

    public void setSelectedFoodie(String userId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        loginUser.setInterestedFoodie(userId);
        System.out.println(loginUser.getUserId());
        System.out.println(userId);
        dbRef.child("Users").child(loginUser.getUserId()).child("interestedFoodie").setValue(userId);
        Toast.makeText(this, "Foodie match sent!", Toast.LENGTH_SHORT).show();
    }

    private String[] getAllFoodieLanguages(List<User> matchedFoodies) {
        List<String> newList = new ArrayList<>();

        for (User foodie : matchedFoodies) {
            if (!allLanguages.contains(foodie.getLanguages())) {
                newList.addAll(foodie.getLanguages());
            }
        }
        Set<String> set = new HashSet<>(newList);
        allLanguages.addAll(set);

        return allLanguages.toArray(new String[0]);
    }



    private Dialog makeSelectDialog(final String[] itemBank, boolean[] checkedItems,
                                    final ArrayList<Integer> itemIndex,
                                    final ArrayList<String> userItems,
                                    final TextView itemSelected) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FoodieResultActivity.this);
        dialogBuilder.setMultiChoiceItems(itemBank, checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            itemIndex.add(which);
                        } else {
                            itemIndex.remove(Integer.valueOf(which));
                        }

                    }
                });

        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder language = new StringBuilder();

                for (int i = 0; i < itemIndex.size(); i++) {
                    language.append(itemBank[itemIndex.get(i)]);
                    userItems.add(itemBank[itemIndex.get(i)]);
                    selectedFoodieLanguages.add(itemBank[itemIndex.get(i)]);
                    if (i != itemIndex.size() - 1) {
                        language.append(", ");
                    }
                }
                itemSelected.setText(language);
            }
        });
        return dialogBuilder.create();
    }
}