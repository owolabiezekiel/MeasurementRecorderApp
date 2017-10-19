package owolabi.tobiloba.measurementrecorder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import owolabi.tobiloba.measurementrecorder.database.RecordContract.RecordEntry;

/**
 * Created by TOBILOBA on 10/10/2017.
 */

public class RecordDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "measurement.db";
    private static final int DATABASE_VERSION = 2;

    public RecordDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_RECORDS_TABLE = "CREATE TABLE " + RecordEntry.TABLE_NAME + "("
                + RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecordEntry.COLUMN_CLIENT_TITLE + " TEXT NOT NULL, "
                + RecordEntry.COLUMN_CLIENT_NAME + " TEXT NOT NULL, "
                + RecordEntry.COLUMN_CLIENT_GENDER+ " INTEGER NOT NULL, "
                + RecordEntry.COLUMN_HEAD + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_NECK + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_NECKLINE + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_BUST_POINT + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_UNDER_BUST + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_BUST + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_WAIST + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_HIP + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_SHOULDER + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_CHEST + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_GOWN_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_BLOUSE_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_SHORT_GOWN_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_SLEEVE_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_ARMHOLE + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_KNEE_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_HALF_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_TROUSER_LENGTH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_THIGH + " INTEGER DEFAULT 0,"
                + RecordEntry.COLUMN_TROUSER_BOTTOM + " INTEGER DEFAULT 0);";

        db.execSQL(SQL_CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
