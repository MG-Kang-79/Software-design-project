package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    TextView tvReservations;
    EditText etReservationId;
    Button btnCancelReservation;

    UserDatabaseHelper db;

    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        studentId =
                getIntent()
                        .getStringExtra(
                                "studentId");

        tvReservations =
                findViewById(R.id.tvReservations);

        etReservationId =
                findViewById(R.id.etReservationId);

        btnCancelReservation =
                findViewById(R.id.btnCancelReservation);

        db = new UserDatabaseHelper(this);

        loadReservations();

        btnCancelReservation.setOnClickListener(v -> {

            String idText =
                    etReservationId.getText().toString();

            if(idText.isEmpty())
                return;

            int id =
                    Integer.parseInt(idText);

            boolean success =
                    db.cancelReservation(id);

            if(success){

                Toast.makeText(
                        this,
                        "Reservation Cancelled",
                        Toast.LENGTH_SHORT
                ).show();

                loadReservations();

            }else{

                Toast.makeText(
                        this,
                        "Cancel Failed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadReservations() {

        Cursor cursor =
                db.getReservationsByStudent(
                        studentId);

        StringBuilder result =
                new StringBuilder();

        while(cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String room =
                    cursor.getString(2);

            String date =
                    cursor.getString(3);

            String time =
                    cursor.getString(4);

            String status =
                    cursor.getString(5);

            result.append("ID : ")
                    .append(id)
                    .append("\n");

            result.append("Room : ")
                    .append(room)
                    .append("\n");

            result.append("Date : ")
                    .append(date)
                    .append("\n");

            result.append("Time : ")
                    .append(time)
                    .append("\n");

            result.append("Status : ")
                    .append(status)
                    .append("\n");

            result.append("------------------------\n");
        }

        tvReservations.setText(
                result.toString());

        cursor.close();
    }
}