package edu.neu.foodiefriendfinder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import edu.neu.foodiefriendfinder.models.User;

public class CreateUserActivity extends AppCompatActivity {

    private boolean[] selectedItems;
    private boolean[] clickedItem;

    private ArrayList<Integer> userItems = new ArrayList<>();

    private Spinner cuisineDropDown;
    private Spinner languagesDropDown;
    private Spinner genderDropDown;

    private EditText userId;
    private EditText userFirstName;
    private EditText userLastName;
    private EditText userPhone;
    private EditText userDob;
    private EditText userEmail;

    private String[] languageOptions;

    private DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userId = findViewById(R.id.userId);
        userFirstName = findViewById(R.id.firstName);
        userLastName = findViewById(R.id.lastName);
        userPhone = findViewById(R.id.phone);
        userDob = findViewById(R.id.dobId);
        userEmail = findViewById(R.id.emailId);

        cuisineDropDown = (Spinner) findViewById(R.id.cuisineSpinner);
        genderDropDown = (Spinner) findViewById(R.id.genderSpinner);
        languagesDropDown = (Spinner) findViewById(R.id.languagesSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(CreateUserActivity.this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.genderOptions));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderDropDown.setAdapter(myAdapter);

        languagesDropDown.setOnTouchListener(spinnerOnTouch);
        languagesDropDown.setOnKeyListener(spinnerOnKey);


        Button register = findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewUser();
            }
        });

        Button goLogin = findViewById(R.id.goLoginButton);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginActivity();
            }
        });

    }

    // https://stackoverflow.com/questions/3928071/setting-a-spinner-onclicklistener-in-android
    private View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                multiCheckBoxBuilder();
                System.out.println("Called multicheckbox");
            }
            return true;
        }
    };

    private static View.OnKeyListener spinnerOnKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void multiCheckBoxBuilder(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your language");

        languageOptions = getResources().getStringArray(R.array.languageOptions);
        selectedItems = new boolean[languageOptions.length];
        builder.setCancelable(false);
        builder.setMultiChoiceItems(languageOptions, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                if (isChecked) {
//                    if (!userItems.contains(which)) {
//                        userItems.add(which);
//                    }
//                }
//                else if (userItems.contains(which)) {
//                    userItems.remove(which);
//                }

                clickedItem[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                String option = "";
//                for (int i = 0; i < userItems.size(); i++) {
//                    option = option + languageOptions[userItems.get(i)];
//
//                    if (i != userItems.size() -1 ) {
//                        option = option + ", ";
//                    }
//                }
                selectedItems = clickedItem;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedItems.length; i++) {
                    selectedItems[i] = false;
                    userItems.clear();
                    Toast.makeText(getApplicationContext(),"Cleared all", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void createNewUser() {
        String id = userId.getText().toString();
        String firstName = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String email = userEmail.getText().toString();
        String phone = userPhone.getText().toString();
        String dob = userDob.getText().toString();
        String gender = genderDropDown.getSelectedItem().toString();
        if (!id.equals("") && !firstName.equals("") && !lastName.equals("") && !email.equals("") &&
                !phone.equals("") && !dob.equals("") && !gender.equals("")) {
            User newUser = new User(id, firstName, lastName, email, phone, dob, gender);
            usersReference.child("Users").child(id).setValue(newUser);
            Toast.makeText(this, "Success!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Something unfilled, please check!", Toast.LENGTH_SHORT).show();

        }
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
