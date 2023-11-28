package com.example.th8;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "ql.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    private static final String TABLE_NAME = "nhanvien";
    private static final String COLUMN_ID_NV = "manv";
    private static final String COLUMN_TEN = "tennv";
    private static final String COLUMN_ID_phong = "maph";

    private static final String TABLE_NAME2 = "phongban";
    private static final String COLUMN_MOTA = "mota";
    private static final String COLUMN_NAME_PHONG = "tenph";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID_NV + " INTERGER PRIMARY KEY,"
                + COLUMN_TEN + " VARCHAR,"
                + COLUMN_ID_phong+ " INTERGER" + ")";
        String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + "("
                + COLUMN_ID_phong + " INTERGER PRIMARY KEY,"
                + COLUMN_NAME_PHONG + " VARCHAR,"
                + COLUMN_MOTA + " VARCHAR"+ ")";
        // Execute SQL statement
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
    }

    // Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // SQL statement to drop table
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String DROP_TABLE2 = "DROP TABLE IF EXISTS " + TABLE_NAME2;
        // Execute SQL statement
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE2);
        // Create table again
        onCreate(db);
    }

    // Add PhongBan
    public void addPhong(PhongBan phong) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values to store data
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_phong, phong.getMaph());
        values.put(COLUMN_NAME_PHONG, phong.getTenph());  // Corrected line
        values.put(COLUMN_MOTA, phong.getMota());  // Corrected line
        // Insert data into the table
        db.insert(TABLE_NAME2, null, values);
        // Close the database
        db.close();
    }


    // Add Product
    public void addNhanvien(NhanVien nv) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create content values to store data
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_NV, nv.getManv());
        values.put(COLUMN_TEN, nv.getTennv());
        values.put(COLUMN_ID_phong,nv.getMaph());

        // Insert data into table
        db.insert(TABLE_NAME, null, values);
        // Close database
        db.close();
    }
    //Get all phong
    @SuppressLint("Range")
    public ArrayList<PhongBan> getAllListPhong() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<PhongBan> list = new ArrayList<>();
        // SQL statement to select all records
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME2;
        // Execute SQL statement and get cursor
        Cursor cursor = db.query(TABLE_NAME2, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                PhongBan p = new PhongBan();
                p.setMaph((cursor.getInt(cursor.getColumnIndex(COLUMN_ID_phong))));
                p.setTenph(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONG)));
                // Add category to list
                list.add(p);
            } while (cursor.moveToNext());
        }
        // Close cursor and database
        cursor.close();
        db.close();

        return list;
    }
    public ArrayList<String> getAllNamephong() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<String> Names = new ArrayList<>();

        String SELECT_ALL_NAMES = "SELECT " + COLUMN_NAME_PHONG + " FROM " + TABLE_NAME2;
        // Execute SQL statement and get cursor
        Cursor cursor = db.rawQuery(SELECT_ALL_NAMES, null);
        // Loop through cursor and add category names to the list
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String Name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONG));
                Names.add(Name);
            } while (cursor.moveToNext());
        }
        // Close cursor and database
        cursor.close();
        db.close();
        // Return list of category names
        return Names;
    }


    // Get all Nhan vien
    @SuppressLint("Range")
    public ArrayList<NhanVien> getAllnv() {
        // Get readable database
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<NhanVien> list = new ArrayList<>();
        // SQL statement to select all records
        String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
        // Execute SQL statement and get cursor
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        // Loop through cursor and create product
        if (cursor.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien();
                nv.setManv((cursor.getInt(cursor.getColumnIndex(COLUMN_ID_NV))));
                nv.setTennv(cursor.getString(cursor.getColumnIndex(COLUMN_TEN)));
                nv.setMaph(cursor.getInt(cursor.getColumnIndex(COLUMN_ID_phong)));

                // Add product to list
                list.add(nv);
            } while (cursor.moveToNext());
        }
        // Close cursor and database
        cursor.close();
        db.close();
        return list;
    }

    // Delete product
    public void deleteNhanVien(NhanVien nv) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // SQL statement to delete record
        String DELETE = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID_NV + " = ?";
        // Execute SQL statement with student id as argument
        db.delete(TABLE_NAME, COLUMN_ID_NV + " = ?", new String[]{String.valueOf(nv.getManv())});
        // Close database
        db.close();
    }
    public ArrayList<NhanVien> getEmployeesByPhongBan(int phongBanId) {
        ArrayList<NhanVien> employeeList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                "manv",
                "tennv",
                "maph"
        };

        // Specify the selection criteria
        String selection = "maph = ?";
        String[] selectionArgs = {String.valueOf(phongBanId)};

        // Query the database
        Cursor cursor = db.query(
                TABLE_NAME,         // The table name
                projection,         // The columns to retrieve
                selection,          // The columns for the WHERE clause
                selectionArgs,      // The values for the WHERE clause
                null,               // Don't group the rows
                null,               // Don't filter by row groups
                null                // The sort order
        );

        // Iterate through the result set and populate the list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_NV));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN));
                int phongBanIdResult = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID_phong));

                // Create a new NhanVien object and add it to the list
                NhanVien nhanVien = new NhanVien(id, name, phongBanIdResult);
                employeeList.add(nhanVien);
            }
            cursor.close();
        }

        // Close the database connection
        db.close();

        return employeeList;
    }



}
