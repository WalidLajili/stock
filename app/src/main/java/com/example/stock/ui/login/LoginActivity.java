package com.example.stock.ui.login;



import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;


import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.stock.Home;
import com.example.stock.R;
import com.google.android.material.snackbar.Snackbar;


public class LoginActivity extends AppCompatActivity {

    String USER_NAME = "admin";
    String PASSWORD = "admin";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginActivity self = this;
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);


        loginButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideSoftKeyBoard();
                                String userName = usernameEditText.getText().toString();
                                String password = passwordEditText.getText().toString();

                                if (userName.equals(USER_NAME) && password.equals(PASSWORD)) {
                                    Intent intent = new Intent(self, Home.class);
                                    startActivity(intent);
                                } else {
                                    Snackbar
                                            .make(
                                                    view,
                                                  "Verifier Nom et mot de passe",
                                                    Snackbar.LENGTH_LONG
                                            )
                                            .show();
                                }
                            }

                        });

    }

    public void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                INPUT_METHOD_SERVICE
        );

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}