package com.example.cinefast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private ImageView ivBack;

    // Firebase instances
    // TODO: Requires google-services.json in app/ directory
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        ivBack = findViewById(R.id.ivBack);

        ivBack.setOnClickListener(v -> finish());
        btnRegister.setOnClickListener(v -> attemptRegister());

        // "Already have an account? Log In" link
        TextView tvLogin = findViewById(R.id.tvLogin);
        if (tvLogin != null) {
            tvLogin.setOnClickListener(v -> finish()); // go back to LoginActivity
        }
    }

    private void attemptRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validate fields
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (password.length() < 8) {
            etPassword.setError("Password must be at least 8 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        btnRegister.setEnabled(false);

        // TODO: Firebase Authentication — requires google-services.json + Firebase project setup
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Store user data in Firebase Realtime Database under users/{uid}
                            // TODO: Requires Firebase Realtime Database enabled in Firebase console
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);
                            userData.put("uid", user.getUid());

                            mDatabase.child("users").child(user.getUid())
                                    .setValue(userData)
                                    .addOnCompleteListener(dbTask -> {
                                        // Save session
                                        SharedPreferences prefs = getSharedPreferences(
                                                "cinefast_session_pref_v3", MODE_PRIVATE);
                                        prefs.edit()
                                                .putBoolean("is_logged_in", true)
                                                .putString("user_email", email)
                                                .apply();

                                        Toast.makeText(SignupActivity.this,
                                                "Registration successful!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    });
                        }
                    } else {
                        btnRegister.setEnabled(true);
                        String msg = task.getException() != null
                                ? task.getException().getMessage()
                                : "Registration failed";
                        Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
