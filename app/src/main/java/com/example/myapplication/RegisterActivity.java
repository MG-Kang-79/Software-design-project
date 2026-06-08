package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etStudentId;
    EditText etPassword;
    EditText etPasswordCheck;
    EditText etName;
    EditText etDepartment;
    EditText etEmail;

    Button btnRegister;
    Button btnCancel;

    UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etStudentId = findViewById(R.id.etStudentId);
        etPassword = findViewById(R.id.etPassword);
        etPasswordCheck = findViewById(R.id.etPasswordCheck);
        etName = findViewById(R.id.etName);
        etDepartment = findViewById(R.id.etDepartment);
        etEmail = findViewById(R.id.etEmail);

        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        dbHelper = new UserDatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {

            String studentId = etStudentId.getText().toString();
            String password = etPassword.getText().toString();
            String passwordCheck = etPasswordCheck.getText().toString();
            String name = etName.getText().toString();
            String department = etDepartment.getText().toString();
            String email = etEmail.getText().toString();

            if(!password.equals(passwordCheck)){
                Toast.makeText(this,
                        "Password mismatch",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db =
                    dbHelper.getWritableDatabase();

            ContentValues values =
                    new ContentValues();

            values.put("studentId", studentId);
            values.put("password", password);
            values.put("name", name);
            values.put("department", department);
            values.put("email", email);

            db.insert("users",
                    null,
                    values);

            Toast.makeText(this,
                    "Registration Complete!",
                    Toast.LENGTH_SHORT).show();

            startActivity(
                    new Intent(
                            RegisterActivity.this,
                            MainActivity.class));

            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}