package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    Button btnSearchRoom;
    LinearLayout layoutRoomList;

    String studentId;

    UserDatabaseHelper db;

    String defaultDate =
            "2026-06-10";

    String[] timeSlots = {
            "09:00 - 10:00",
            "10:00 - 11:00",
            "11:00 - 12:00",
            "13:00 - 14:00",
            "14:00 - 15:00"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        studentId =
                getIntent()
                        .getStringExtra(
                                "studentId");

        db = new UserDatabaseHelper(this);

        btnSearchRoom =
                findViewById(R.id.btnSearchRoom);

        layoutRoomList =
                findViewById(R.id.layoutRoomList);

        btnSearchRoom.setOnClickListener(
                v -> showRooms());
    }

    private void showRooms() {

        layoutRoomList.removeAllViews();

        Cursor cursor =
                db.getRooms();

        while(cursor.moveToNext()) {

            String roomName =
                    cursor.getString(1);

            String capacity =
                    cursor.getString(2);

            String location =
                    cursor.getString(3);

            for(String time : timeSlots) {

                boolean reserved =
                        db.isRoomReserved(
                                roomName,
                                defaultDate,
                                time);

                if(reserved) {

                    addRoomItem(
                            roomName,
                            capacity,
                            location,
                            defaultDate,
                            time,
                            "Reserved");

                } else {

                    addRoomItem(
                            roomName,
                            capacity,
                            location,
                            defaultDate,
                            time,
                            "Available");
                }
            }
        }

        cursor.close();
    }

    private void addRoomItem(String room,
                             String capacity,
                             String location,
                             String date,
                             String time,
                             String status) {

        TextView item =
                new TextView(this);

        item.setText(
                room + "\n" +
                        "Location : " + location + "\n" +
                        "Capacity : " + capacity + "\n" +
                        date + "\n" +
                        time + "\n" +
                        "Status : " + status
        );

        item.setTextSize(18);
        item.setPadding(
                25,
                25,
                25,
                25);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(
                0,
                0,
                0,
                20);

        item.setLayoutParams(params);

        if(status.equals("Available")) {

            item.setBackgroundColor(
                    0xFFE0F5E9);

            item.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                SearchActivity.this,
                                ReservationActivity.class);

                intent.putExtra(
                        "room",
                        room);

                intent.putExtra(
                        "date",
                        date);

                intent.putExtra(
                        "time",
                        time);

                intent.putExtra(
                        "studentId",
                        studentId);

                startActivity(intent);
            });

        } else {

            item.setBackgroundColor(
                    0xFFFFE1E1);
        }

        layoutRoomList.addView(item);
    }
}