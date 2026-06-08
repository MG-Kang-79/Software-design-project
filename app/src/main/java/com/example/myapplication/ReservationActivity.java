package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReservationActivity extends AppCompatActivity {

    EditText etRoom;
    EditText etDate;
    EditText etTime;

    Button btnReserve;

    UserDatabaseHelper db;

    String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        etRoom =
                findViewById(R.id.etRoom);

        etDate =
                findViewById(R.id.etDate);

        etTime =
                findViewById(R.id.etTime);

        btnReserve =
                findViewById(R.id.btnReserve);

        db = new UserDatabaseHelper(this);

        studentId =
                getIntent()
                        .getStringExtra(
                                "studentId");

        String room =
                getIntent()
                        .getStringExtra(
                                "room");

        String date =
                getIntent()
                        .getStringExtra(
                                "date");

        String time =
                getIntent()
                        .getStringExtra(
                                "time");

        if(room != null) {
            etRoom.setText(room);
        }

        if(date != null) {
            etDate.setText(date);
        }

        if(time != null) {
            etTime.setText(time);
        }

        btnReserve.setOnClickListener(v -> {

            String selectedRoom =
                    etRoom.getText().toString();

            String selectedDate =
                    etDate.getText().toString();

            String selectedTime =
                    etTime.getText().toString();

            boolean success =
                    db.reserveRoom(
                            studentId,
                            selectedRoom,
                            selectedDate,
                            selectedTime);

            if(success){

                Toast.makeText(
                        this,
                        "Reservation Request Completed",
                        Toast.LENGTH_SHORT
                ).show();

            }else{

                Toast.makeText(
                        this,
                        "Restricted User or Already Reserved",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}