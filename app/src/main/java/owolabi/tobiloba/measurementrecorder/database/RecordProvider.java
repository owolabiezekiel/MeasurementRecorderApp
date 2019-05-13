package owolabi.tobiloba.measurementrecorder.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import owolabi.tobiloba.measurementrecorder.database.RecordContract.RecordEntry;

/**
 * Created by TOBILOBA on 10/10/2017.
 */

public class RecordProvider extends ContentProvider {
    public static final String LOG_TAG = RecordProvider.class.getSimpleName();

    private RecordDBHelper mDbHelper;
    private static final int RECORDS = 100;
    private static final int RECORD_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(RecordContract.CONTENT_AUTHORITY, RecordContract.PATH_RECORDS, RECORDS);
        sUriMatcher.addURI(RecordContract.CONTENT_AUTHORITY, RecordContract.PATH_RECORDS + "/#", RECORD_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new RecordDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case RECORDS:
                cursor = db.query(RecordEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case RECORD_ID:
                selection = RecordEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(RecordEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, RecordEntry.COLUMN_CLIENT_NAME);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }



    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return RecordEntry.CONTENT_LIST_TYPE;
            case RECORD_ID:
                return RecordEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        String name = values.getAsString(RecordEntry.COLUMN_CLIENT_NAME);
        String gender = values.getAsString(RecordEntry.COLUMN_CLIENT_GENDER);
        int head = values.getAsInteger(RecordEntry.COLUMN_HEAD);
        int neck = values.getAsInteger(RecordEntry.COLUMN_NECK);
        int neckline = values.getAsInteger(RecordEntry.COLUMN_NECKLINE);
        int bustPoint = values.getAsInteger(RecordEntry.COLUMN_BUST_POINT);
        int underBust = values.getAsInteger(RecordEntry.COLUMN_UNDER_BUST);
        int bust = values.getAsInteger(RecordEntry.COLUMN_BUST);
        int waist = values.getAsInteger(RecordEntry.COLUMN_WAIST);
        int hip = values.getAsInteger(RecordEntry.COLUMN_HIP);
        int shoulder = values.getAsInteger(RecordEntry.COLUMN_SHOULDER);
        int chest = values.getAsInteger(RecordEntry.COLUMN_CHEST);
        int gownLength = values.getAsInteger(RecordEntry.COLUMN_GOWN_LENGTH);
        int blouseLength = values.getAsInteger(RecordEntry.COLUMN_BLOUSE_LENGTH);
        int shortGownLength = values.getAsInteger(RecordEntry.COLUMN_SHORT_GOWN_LENGTH);
        int sleeveLength = values.getAsInteger(RecordEntry.COLUMN_SLEEVE_LENGTH);
        int armhole = values.getAsInteger(RecordEntry.COLUMN_ARMHOLE);
        int kneeLength = values.getAsInteger(RecordEntry.COLUMN_KNEE_LENGTH);
        int halfLength = values.getAsInteger(RecordEntry.COLUMN_HALF_LENGTH);
        int trouserLength = values.getAsInteger(RecordEntry.COLUMN_TROUSER_LENGTH);
        int thigh = values.getAsInteger(RecordEntry.COLUMN_THIGH);
        int trouserBottom = values.getAsInteger(RecordEntry.COLUMN_TROUSER_BOTTOM);


        // Check that the name is not null
        if (name == null) {
            throw new IllegalArgumentException("Client requires a name");
        }

        // Check that the gender is not null
        if (gender == null) {
            throw new IllegalArgumentException("Client requires a gender");
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(RecordEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                // Delete all rows that match the selection and selection args
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(RecordEntry.TABLE_NAME, selection, selectionArgs);
            case RECORD_ID:
                // Delete a single row given by the ID in the URI
                selection = RecordEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(RecordEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }


    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case RECORD_ID:
                selection = RecordEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
       // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(RecordEntry.COLUMN_CLIENT_NAME)) {
            String name = values.getAsString(RecordEntry.COLUMN_CLIENT_NAME);
            if (name == null ) {
                throw new IllegalArgumentException("Client requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(RecordEntry.COLUMN_CLIENT_GENDER)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer gender = values.getAsInteger(RecordEntry.COLUMN_CLIENT_GENDER);
            if (gender == null || !RecordEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Client requires valid gender");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int numOfRowsAffected = database.update(RecordEntry.TABLE_NAME, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return numOfRowsAffected;
    }

    private int getCount(){
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String allRecords = "SELECT * FROM records";
        Cursor cursor = database.rawQuery(allRecords, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
