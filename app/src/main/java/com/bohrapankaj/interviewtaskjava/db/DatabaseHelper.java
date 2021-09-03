package com.bohrapankaj.interviewtaskjava.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "company_database";
    private static final String TABLE_NAME = "company";
    private static final String COLOM_1 = "id";
    private static final String COLOM_2 = "n_product_uniq_id";
    private static final String COLOM_3 = "n_state_uniq_id";
    private static final String COLOM_4 = "c_state_name";
    private static final String COLOM_5 = "effective_from";
    private static final String COLOM_6 = "c_added_by_admin_user_id";
    private static final String COLOM_7 = "product_rate";
    private static final String COLOM_8 = "added_time";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // here we will create the database table
        db.execSQL(" create table " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, n_product_uniq_id TEXT,n_state_uniq_id TEXT,c_state_name TEXT" +
                ",effective_from TEXT,c_added_by_admin_user_id TEXT,product_rate TEXT,added_time TEXT)");

    }

    // if we want to add more field we have to upgrade databse
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean InsertData(
            String n_product_uniq_id, String n_state_uniq_id,
            String c_state_name, String effective_from,
            String c_added_by_admin_user_id, String product_rate,
            String added_time
    ) {
        SQLiteDatabase database = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOM_2, n_product_uniq_id);
        contentValues.put(COLOM_3, n_state_uniq_id);
        contentValues.put(COLOM_4, c_state_name);
        contentValues.put(COLOM_5, effective_from);
        contentValues.put(COLOM_6, c_added_by_admin_user_id);
        contentValues.put(COLOM_7, product_rate);
        contentValues.put(COLOM_8, added_time);

        long result = database.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor RetriveData() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME, null);
        return cursor;
    }

    public boolean UpdateData(String id, String price) {
        SQLiteDatabase database = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOM_7, price);

        database.update(TABLE_NAME, contentValues, "ID=?", new String[]{id});
        return true;
    }
}
