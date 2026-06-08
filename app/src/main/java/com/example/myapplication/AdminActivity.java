package com.example.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    ListView listReservations;

    Button btnApprove;
    Button btnReject;

    Button btnRestrictUser;
    Button btnAddRoom;

    EditText etRestrictStudentId;

    EditText etRoomName;
    EditText etCapacity;
    EditText etLocation;

    UserDatabaseHelper db;

    ArrayList<String> reservations;
    ArrayList<Integer> reservationIds;

    int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listReservations =
                findViewById(R.id.listReservations);

        btnApprove =
                findViewById(R.id.btnApprove);

        btnReject =
                findViewById(R.id.btnReject);

        etRestrictStudentId =
                findViewById(R.id.etRestrictStudentId);

        btnRestrictUser =
                findViewById(R.id.btnRestrictUser);

        etRoomName =
                findViewById(R.id.etRoomName);

        etCapacity =
                findViewById(R.id.etCapacity);

        etLocation =
                findViewById(R.id.etLocation);

        btnAddRoom =
                findViewById(R.id.btnAddRoom);

        db = new UserDatabaseHelper(this);

        reservations =
                new ArrayList<>();

        reservationIds =
                new ArrayList<>();

        loadReservations();

        listReservations.setOnItemClickListener(
                (parent, view, position, id) -> {

                    selectedId =
                            reservationIds.get(position);

                    Toast.makeText(
                            this,
                            "Selected Reservation ID : " + selectedId,
                            Toast.LENGTH_SHORT
                    ).show();
                });

        btnApprove.setOnClickListener(v -> {

            if(selectedId == -1){

                Toast.makeText(
                        this,
                        "Select Reservation First",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            db.updateReservationStatus(
                    selectedId,
                    "Approved");

            Toast.makeText(
                    this,
                    "Reservation Approved",
                    Toast.LENGTH_SHORT
            ).show();

            loadReservations();
        });

        btnReject.setOnClickListener(v -> {

            if(selectedId == -1){

                Toast.makeText(
                        this,
                        "Select Reservation First",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            db.updateReservationStatus(
                    selectedId,
                    "Rejected");

            Toast.makeText(
                    this,
                    "Reservation Rejected",
                    Toast.LENGTH_SHORT
            ).show();

            loadReservations();
        });

        btnRestrictUser.setOnClickListener(v -> {

            String studentId =
                    etRestrictStudentId
                            .getText()
                            .toString();

            boolean success =
                    db.restrictUser(studentId);

            if(success){

                Toast.makeText(
                        this,
                        "User Restricted",
                        Toast.LENGTH_SHORT
                ).show();

            }else{

                Toast.makeText(
                        this,
                        "Already Restricted",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnAddRoom.setOnClickListener(v -> {

            String roomName =
                    etRoomName.getText().toString();

            String capacity =
                    etCapacity.getText().toString();

            String location =
                    etLocation.getText().toString();

            boolean success =
                    db.addRoom(
                            roomName,
                            capacity,
                            location);

            if(success){

                Toast.makeText(
                        this,
                        "Room Added",
                        Toast.LENGTH_SHORT
                ).show();

                etRoomName.setText("");
                etCapacity.setText("");
                etLocation.setText("");

            }else{

                Toast.makeText(
                        this,
                        "Add Failed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadReservations() {

        reservations.clear();
        reservationIds.clear();

        Cursor cursor =
                db.getPendingReservations();

        while(cursor.moveToNext()) {

            int id =
                    cursor.getInt(0);

            String studentId =
                    cursor.getString(1);

            String room =
                    cursor.getString(2);

            String date =
                    cursor.getString(3);

            String time =
                    cursor.getString(4);

            reservations.add(
                    "ID : " + id +
                            "\nStudent : " + studentId +
                            "\nRoom : " + room +
                            "\nDate : " + date +
                            "\nTime : " + time
            );

            reservationIds.add(id);
        }

        cursor.close();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        reservations);

        listReservations.setAdapter(adapter);
    }
}