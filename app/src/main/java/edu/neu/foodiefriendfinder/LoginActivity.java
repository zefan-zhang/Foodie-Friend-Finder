package edu.neu.foodiefriendfinder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button signInButton;

    EditText usernameInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.SignInButton);

        usernameInput = findViewById(R.id.usernameInput);


    }
}
