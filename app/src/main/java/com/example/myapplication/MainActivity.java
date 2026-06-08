package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etStudentId;
    EditText etPassword;

    Button btnLogin;
    Button btnRegister;

    UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        dbHelper = new UserDatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {

            String studentId = etStudentId.getText().toString();
            String password = etPassword.getText().toString();

            if (studentId.equals("admin") && password.equals("admin123")) {
                startActivity(
                        new Intent(
                                MainActivity.this,
                                AdminActivity.class));
                return;
            }

            boolean success =
                    dbHelper.checkLogin(studentId, password);

            if (success) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                HomeActivity.class);

                intent.putExtra(
                        "studentId",
                        studentId);

                startActivity(intent);

            } else {
                Toast.makeText(
                        this,
                        "Login Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(
                    new Intent(
                            MainActivity.this,
                            RegisterActivity.class));
        });
    }
}