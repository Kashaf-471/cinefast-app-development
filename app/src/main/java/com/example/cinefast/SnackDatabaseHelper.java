package com.example.cinefast;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SnackDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinefast_snacks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SNACKS = "snacks";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE_NAME = "image_name";

    private Context context;

    public SnackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create snacks table
        String createTable = "CREATE TABLE " + TABLE_SNACKS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_DESCRIPTION + " TEXT, "
                + COL_PRICE + " REAL NOT NULL, "
                + COL_IMAGE_NAME + " TEXT"
                + ")";
        db.execSQL(createTable);

        // Insert initial snack data on first run
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SNACKS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        db.insert(TABLE_SNACKS, null, buildSnack("Popcorn", "Large / Buttered", 8.99, "placeholder"));
        db.insert(TABLE_SNACKS, null, buildSnack("Nachos", "With Cheese Dip", 7.99, "nachos"));
        db.insert(TABLE_SNACKS, null, buildSnack("Soft Drink", "Large / Any Flavor", 5.99, "drinks"));
        db.insert(TABLE_SNACKS, null, buildSnack("Candy Mix", "Assorted Candies", 6.99, "candy"));
    }

    private ContentValues buildSnack(String name, String description, double price, String imageName) {
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        cv.put(COL_DESCRIPTION, description);
        cv.put(COL_PRICE, price);
        cv.put(COL_IMAGE_NAME, imageName);
        return cv;
    }

    /**
     * Fetch all snacks from database and resolve drawable resource IDs.
     */
    public ArrayList<Snack> getAllSnacks() {
        ArrayList<Snack> snacks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SNACKS, null, null, null, null, null, COL_ID + " ASC");

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                String imageName = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE_NAME));

                // Resolve drawable resource ID from image name string
                int resId = context.getResources().getIdentifier(
                        imageName, "drawable", context.getPackageName());
                if (resId == 0) {
                    resId = R.drawable.placeholder;
                }

                snacks.add(new Snack(name, description, price, resId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return snacks;
    }
}
