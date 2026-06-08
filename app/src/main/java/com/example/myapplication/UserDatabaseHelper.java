package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserDB";
    private static final int DB_VERSION = 3;

    public UserDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String userSql =
                "CREATE TABLE users (" +
                        "studentId TEXT PRIMARY KEY," +
                        "password TEXT," +
                        "name TEXT," +
                        "department TEXT," +
                        "email TEXT)";

        db.execSQL(userSql);

        String reservationSql =
                "CREATE TABLE reservations (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "studentId TEXT," +
                        "room TEXT," +
                        "date TEXT," +
                        "time TEXT," +
                        "status TEXT)";

        db.execSQL(reservationSql);

        String roomSql =
                "CREATE TABLE rooms (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "roomName TEXT," +
                        "capacity TEXT," +
                        "location TEXT)";

        db.execSQL(roomSql);

        String restrictSql =
                "CREATE TABLE restricted_users (" +
                        "studentId TEXT PRIMARY KEY)";

        db.execSQL(restrictSql);

        insertDefaultRooms(db);
    }

    private void insertDefaultRooms(SQLiteDatabase db) {

        ContentValues r1 = new ContentValues();
        r1.put("roomName", "Room 101");
        r1.put("capacity", "4");
        r1.put("location", "Library 1F");
        db.insert("rooms", null, r1);

        ContentValues r2 = new ContentValues();
        r2.put("roomName", "Room 204");
        r2.put("capacity", "6");
        r2.put("location", "Engineering Building");
        db.insert("rooms", null, r2);

        ContentValues r3 = new ContentValues();
        r3.put("roomName", "Room 305");
        r3.put("capacity", "8");
        r3.put("location", "Central Library");
        db.insert("rooms", null, r3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS reservations");
        db.execSQL("DROP TABLE IF EXISTS rooms");
        db.execSQL("DROP TABLE IF EXISTS restricted_users");

        onCreate(db);
    }

    public boolean checkLogin(String studentId,
                              String password) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM users WHERE studentId=? AND password=?",
                        new String[]{studentId, password});

        boolean result =
                cursor.moveToFirst();

        cursor.close();

        return result;
    }

    public boolean registerUser(String studentId,
                                String password,
                                String name,
                                String department,
                                String email) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("studentId", studentId);
        values.put("password", password);
        values.put("name", name);
        values.put("department", department);
        values.put("email", email);

        long result =
                db.insert("users", null, values);

        return result != -1;
    }

    public boolean reserveRoom(String studentId,
                               String room,
                               String date,
                               String time) {

        if (isRestricted(studentId)) {
            return false;
        }

        SQLiteDatabase db =
                this.getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM reservations " +
                                "WHERE room=? AND date=? AND time=? " +
                                "AND status!='Rejected' AND status!='Cancelled'",
                        new String[]{room, date, time});

        boolean alreadyReserved =
                cursor.moveToFirst();

        cursor.close();

        if (alreadyReserved) {
            return false;
        }

        ContentValues values =
                new ContentValues();

        values.put("studentId", studentId);
        values.put("room", room);
        values.put("date", date);
        values.put("time", time);
        values.put("status", "Pending");

        long result =
                db.insert("reservations", null, values);

        return result != -1;
    }

    public boolean isRoomReserved(String room,
                                  String date,
                                  String time) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM reservations " +
                                "WHERE room=? AND date=? AND time=? " +
                                "AND status!='Rejected' AND status!='Cancelled'",
                        new String[]{room, date, time});

        boolean result =
                cursor.moveToFirst();

        cursor.close();

        return result;
    }

    public Cursor getReservations() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM reservations",
                null);
    }

    public Cursor getPendingReservations() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM reservations WHERE status='Pending'",
                null);
    }

    public Cursor getReservationsByStudent(String studentId) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM reservations WHERE studentId=?",
                new String[]{studentId});
    }

    public boolean updateReservationStatus(int id,
                                           String status) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("status", status);

        int result =
                db.update(
                        "reservations",
                        values,
                        "id=?",
                        new String[]{String.valueOf(id)});

        return result > 0;
    }

    public boolean cancelReservation(int id) {

        return updateReservationStatus(id, "Cancelled");
    }

    public Cursor getRooms() {

        SQLiteDatabase db =
                this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM rooms",
                null);
    }

    public boolean addRoom(String roomName,
                           String capacity,
                           String location) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("roomName", roomName);
        values.put("capacity", capacity);
        values.put("location", location);

        long result =
                db.insert("rooms", null, values);

        return result != -1;
    }

    public boolean restrictUser(String studentId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("studentId", studentId);

        long result =
                db.insert("restricted_users", null, values);

        return result != -1;
    }

    public boolean isRestricted(String studentId) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM restricted_users WHERE studentId=?",
                        new String[]{studentId});

        boolean result =
                cursor.moveToFirst();

        cursor.close();

        return result;
    }
}