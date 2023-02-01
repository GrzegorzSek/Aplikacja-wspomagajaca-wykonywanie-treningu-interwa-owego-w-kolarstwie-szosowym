package com.example.projekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;


public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TAG = "DatabaseHelper";

//   table and column names
    private static final String TABLE_NAME = "traning_plan";
    private static final String ID = "training_plan_id";
    private static final String PLAN_TRAINING_NAME = "name";
    private static final String WARM_UP = "warm_up"; // warm up time
    private static final String LOW_INTST = "low"; // Low intensity time
    private static final String HIGH_INTST = "high"; // High intensity time
    private static final String SETS = "sets"; // number of sets (low/high)
    private static final String REST = "rest"; // time between cycles
    private static final String COOL_DOWN = "cool_down"; // time after training
    private static final String CYCLES = "cycles"; // number of sets repetitions

    public DatabaseHelper(Context context) { // default constructor
        super(context, TABLE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // only run when the database file did not exist and was just created
        String createTable = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, ID, PLAN_TRAINING_NAME, WARM_UP, LOW_INTST, HIGH_INTST, SETS, REST, COOL_DOWN, CYCLES);

        db.execSQL(createTable);
    }

    @Override
    // only called when the database file exists but the stored version number is lower than requested in the constructor
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_NAME));
        onCreate(db);
    }

    public boolean addData(String name, String warm_up, String low_intst, String high_intst, String sets, String rest, String cool_down, String cycles){
        SQLiteDatabase db = this.getWritableDatabase(); // create an SQLite database object
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAN_TRAINING_NAME, name);
        contentValues.put(WARM_UP, warm_up);
        contentValues.put(LOW_INTST, low_intst);
        contentValues.put(HIGH_INTST, high_intst);
        contentValues.put(SETS, sets);
        contentValues.put(REST, rest);
        contentValues.put(COOL_DOWN, cool_down);
        contentValues.put(CYCLES, cycles);

        Log.d(TAG, "addData: Adding " + PLAN_TRAINING_NAME + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1; // if -1 then false, else -- true
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT %s FROM %s WHERE name = '%s'", ID, TABLE_NAME, name);

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("DELETE FROM %s WHERE training_plan_id = '%s'", TABLE_NAME, id);
        db.execSQL(query);
    }

    public Cursor getFullData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("SELECT %s, %s, %s, %s, %s, %s, %s, %s FROM %s WHERE training_plan_id = '%s'", PLAN_TRAINING_NAME, WARM_UP, LOW_INTST, HIGH_INTST, SETS, REST, COOL_DOWN, CYCLES, TABLE_NAME, id);
        Cursor data = db.rawQuery(query, null);
        Log.d(TAG, "data: " + data);
        return data;
    }

    public void updateData(int id, String name, String warm_up, String low_intst, String high_intst, String sets, String rest, String cool_down, String cycles){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE training_plan_id = '%s'", TABLE_NAME, PLAN_TRAINING_NAME, name, WARM_UP, warm_up, LOW_INTST, low_intst, HIGH_INTST, high_intst, SETS, sets, REST, rest, COOL_DOWN, cool_down, CYCLES, cycles, id);
        db.execSQL(query);
    }
}
