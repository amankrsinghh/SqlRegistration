package com.example.registrationsql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {
    EditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    SqlHelper dbHelper;
    TextView createAccountTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);

        dbHelper = new SqlHelper(this);

        createAccountTxt = findViewById(R.id.create_accountTxt);
       createAccountTxt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent inext = new Intent(login.this, MainActivity.class);
               startActivity(inext);
               finish();
           }
       });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etLoginEmail.getText().toString().trim();
                String password = etLoginPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    etLoginEmail.setError("Required");
                    etLoginPassword.setError("Required");
                } else {
                    boolean isValid = dbHelper.checkUserCredentials(email, password);
                    if (isValid) {
                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userEmail", email);

                        editor.apply();
                        Toast.makeText(login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        // Navigate to a new activity (e.g., HomeActivity)
                        Intent intent = new Intent(login.this, homeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(login.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



                        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}