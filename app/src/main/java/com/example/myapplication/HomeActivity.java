package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button btnReserve;
    Button btnHistory;
    Button btnSearch;

    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        studentId =
                getIntent()
                        .getStringExtra(
                                "studentId");

        btnReserve =
                findViewById(R.id.btnReserve);

        btnHistory =
                findViewById(R.id.btnHistory);

        btnSearch =
                findViewById(R.id.btnSearch);

        btnReserve.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            SearchActivity.class);

            intent.putExtra(
                    "studentId",
                    studentId);

            startActivity(intent);
        });

        btnSearch.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            SearchActivity.class);

            intent.putExtra(
                    "studentId",
                    studentId);

            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            HomeActivity.this,
                            HistoryActivity.class);

            intent.putExtra(
                    "studentId",
                    studentId);

            startActivity(intent);
        });
    }
}