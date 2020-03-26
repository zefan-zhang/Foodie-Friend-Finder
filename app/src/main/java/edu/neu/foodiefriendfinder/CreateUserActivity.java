package edu.neu.foodiefriendfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.neu.foodiefriendfinder.models.User;

public class CreateUserActivity extends AppCompatActivity {

    Spinner cuisineDropDown;
    Spinner languagesDropDown;
    Spinner genderDropDown;

    private EditText userId;
    private EditText userFirstName;
    private EditText userLastName;
    private EditText userPhone;
    private EditText userDob;
    private EditText userEmail;

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
