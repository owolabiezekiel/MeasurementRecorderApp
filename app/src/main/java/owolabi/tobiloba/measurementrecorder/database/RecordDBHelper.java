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
                + RecordEntry.COLUMN_CLIENT_NAME + " TEXT NOT NULL, "
                + RecordEntry.COLUMN_CLIENT_GENDER+ " INTEGER NOT NULL, "
                + RecordEntry.COLUMN_HEAD + " REAL,"
                + RecordEntry.COLUMN_NECK + " REAL,"
                + RecordEntry.COLUMN_NECKLINE + " REAL,"
                + RecordEntry.COLUMN_BUST_POINT + " REAL,"
                + RecordEntry.COLUMN_UNDER_BUST + " REAL,"
                + RecordEntry.COLUMN_BUST + " REAL,"
                + RecordEntry.COLUMN_WAIST + " REAL,"
                + RecordEntry.COLUMN_HIP + " REAL,"
                + RecordEntry.COLUMN_SHOULDER + " REAL,"
                + RecordEntry.COLUMN_CHEST + " REAL,"
                + RecordEntry.COLUMN_GOWN_LENGTH + " REAL,"
                + RecordEntry.COLUMN_BLOUSE_LENGTH + " REAL,"
                + RecordEntry.COLUMN_SHORT_GOWN_LENGTH + " REAL,"
                + RecordEntry.COLUMN_SLEEVE_LENGTH + " REAL,"
                + RecordEntry.COLUMN_ARMHOLE + " REAL,"
                + RecordEntry.COLUMN_KNEE_LENGTH + " REAL,"
                + RecordEntry.COLUMN_HALF_LENGTH + " REAL,"
                + RecordEntry.COLUMN_TROUSER_LENGTH + " REAL,"
                + RecordEntry.COLUMN_THIGH + " REAL,"
                + RecordEntry.COLUMN_TROUSER_BOTTOM + " REAL);";

        db.execSQL(SQL_CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
