package owolabi.tobiloba.measurementrecorder;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import owolabi.tobiloba.measurementrecorder.database.RecordContract.RecordEntry;
import owolabi.tobiloba.measurementrecorder.database.RecordDBHelper;
import owolabi.tobiloba.measurementrecorder.model.Measurement;

import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecordDBHelper mDbHelper;
    private static final int RECORD_LOADER = 0;
    private RecordCursorAdapter mCursorAdapter;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;
    private SwipeRefreshLayout swipeContainer;
    private ProgressDialog mProgress;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Banner Ads
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mDbHelper = new RecordDBHelper(this.getBaseContext());
        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setTitle("Hang on...");
        mProgress.setMessage("Signing in...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        //Instantiate Firebase libraries needed-------------------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
        //mDatabaseReference = mDatabase.getReference("users/" + mUser.getUid());

        //--------------------------------------------------------------------------------------------------------


        //  Check Firebase Auth state to set menu items accordingly-----------------------------------------------
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                Toast.makeText(MainActivity.this, "State refreshed", Toast.LENGTH_SHORT).show();
                swipeContainer.setRefreshing(false);
            }
        });
        //--------------------------------------------------------------------------------------------------------


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser == null) {
                    //Toast.makeText(MainActivity.this, "No user is signed in", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(MainActivity.this, "Active account: " + mUser.getEmail(), Toast.LENGTH_LONG).show();
                }
            }
        };


        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        ListView recordListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        recordListView.setEmptyView(emptyView);

        mCursorAdapter = new RecordCursorAdapter(this, null);
        recordListView.setAdapter(mCursorAdapter);

        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(RecordEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(RECORD_LOADER, null, this);
    }


    /**
     * Helper method to delete all records in the database.
     */
    private void deleteAllRecords() {
        int rowsDeleted = getContentResolver().delete(RecordEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from measurement database");
    }

    //--------------------------------------------------------------------confirmation dialogs--------------------------------------------------------------------
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_records_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllRecords();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showDownloadConfirmationDialog() {
        if (checkInternetConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.download_from_cloud_dialog_msg);
            builder.setPositiveButton(R.string.action_download_from_cloud, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    downloadDatabaseFromCloud();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    private void showUploadConfirmationDialog() {
        if (checkInternetConnection()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.upload_to_cloud_dialog_msg);
            builder.setPositiveButton(R.string.action_download_from_cloud, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    uploadDatabaseToCloud();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    //--------------------------------------------------------------------end of confirmation dialogs-----------------------------------------------------------------

    private void signUpOrSignIn() {
        Intent intent = new Intent(MainActivity.this, SignUpLogin.class);
        startActivity(intent);
    }

    private void signOut() {
        Toast.makeText(getApplicationContext(), mUser.getEmail() + " signed out successfully", Toast.LENGTH_LONG).show();
        mAuth.signOut();
        recreate();
    }


    //-----------------------------------------------setting up menu items-----------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        disableSomeMenuItems(menu);
        return true;
    }


    private void disableSomeMenuItems(Menu menu) {
        MenuItem sync = menu.findItem(R.id.action_sync_record_to_cloud);
        MenuItem download = menu.findItem(R.id.action_sync_record_from_cloud);
        MenuItem signIn = menu.findItem(R.id.action_sign_in);
        MenuItem signOut = menu.findItem(R.id.action_sign_out);
        if (mUser == null) {
            sync.setEnabled(false);
            download.setEnabled(false);
            signIn.setEnabled(true);
            signOut.setEnabled(false);
        } else {
            sync.setEnabled(true);
            download.setEnabled(true);
            signIn.setEnabled(false);
            signOut.setEnabled(true);
        }

    }
    //----------------------------------------------end of setting up menu items--------------------------------------------------


    //---------------------------------------------------handle menu clicks-------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" and "Sync Database to Cloud" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;

            case R.id.action_sync_record_to_cloud:
                showUploadConfirmationDialog();
                return true;

            case R.id.action_sign_in:
                signUpOrSignIn();
                return true;

            case R.id.action_sign_out:
                signOut();
                return true;

            case R.id.action_sync_record_from_cloud:
                showDownloadConfirmationDialog();
                return true;

            case R.id.action_suggestion_or_complaints:
                openComplaintSuggestion();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //-------------------------------------------------end of handle menu clicks---------------------------------------------------


    //-------------------------------------------------Everything that has to do with the Loader---------------------------------------------
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                RecordEntry._ID,
                RecordEntry.COLUMN_CLIENT_TITLE,
                RecordEntry.COLUMN_CLIENT_NAME,
                RecordEntry.COLUMN_CLIENT_GENDER
        };

        return new CursorLoader(this, RecordEntry.CONTENT_URI, projection, null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
    //-----------------------------------------end of everything that has to do with the loader------------------------------------------------


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private int getCount() {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String allRecords = "SELECT * FROM records";
        Cursor cursor = database.rawQuery(allRecords, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    private void uploadDatabaseToCloud() {
        if (checkInternetConnection()) {
            mProgress.show();
            SQLiteDatabase database = mDbHelper.getReadableDatabase();
            String allRecords = "SELECT * FROM records";
            final Cursor cursor = database.rawQuery(allRecords, null);
            //Toast.makeText(MainActivity.this, "Moving " + count + " to cloud", Toast.LENGTH_SHORT).show();
            ArrayList<Measurement> measurementArrayList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_TITLE));
                    String name = cursor.getString(cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_NAME));
                    int gender = cursor.getInt(cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_GENDER));
                    float head = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_HEAD));
                    float neck = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_NECK));
                    float neckline = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_NECKLINE));
                    float bustpoint = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_BUST_POINT));
                    float underbust = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_UNDER_BUST));
                    float bust = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_BUST));
                    float waist = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_WAIST));
                    float hip = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_HIP));
                    float shoulder = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_SHOULDER));
                    float chest = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_CHEST));
                    float gownlength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_GOWN_LENGTH));
                    float blouselength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_BLOUSE_LENGTH));
                    float shortGownLength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_SHORT_GOWN_LENGTH));
                    float sleeveLength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_SLEEVE_LENGTH));
                    float armHole = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_ARMHOLE));
                    float kneeLength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_KNEE_LENGTH));
                    float halfLength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_HALF_LENGTH));
                    float trouserLength = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_TROUSER_LENGTH));
                    float thigh = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_THIGH));
                    float trouserBottom = cursor.getFloat(cursor.getColumnIndex(RecordEntry.COLUMN_TROUSER_BOTTOM));


                    Measurement measurement = new Measurement(title, name, gender, head, neck, neckline, bustpoint, underbust, bust, waist, hip,
                            shoulder, chest, gownlength, blouselength, shortGownLength, sleeveLength, armHole, kneeLength, halfLength, trouserLength,
                            thigh, trouserBottom);
                    measurementArrayList.add(measurement);
                } while (cursor.moveToNext());
                mDatabaseReference = mDatabase.getReference("users/" + mUser.getUid());
                mDatabaseReference.child("measurement").setValue(measurementArrayList);
                String record_or_records = measurementArrayList.size() > 1 ? " records to the cloud" : "record to cloud";
                Toast.makeText(MainActivity.this, "Moved " + measurementArrayList.size() + record_or_records, Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        }
    }

    private void downloadDatabaseFromCloud() {

        mDatabaseReference = mDatabase.getReference("users/" + mUser.getUid());
        mDatabaseReference.child("measurement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Toast.makeText(MainActivity.this, "True. It exists", Toast.LENGTH_SHORT).show();
                    deleteAllRecords();
                    int count = 0;
                    for (DataSnapshot dt : dataSnapshot.getChildren()) {
                        Measurement measurement = dt.getValue(Measurement.class);
                        ContentValues values = new ContentValues();

                        values.put(RecordEntry.COLUMN_CLIENT_TITLE, measurement.title);
                        values.put(RecordEntry.COLUMN_CLIENT_NAME, measurement.name);
                        values.put(RecordEntry.COLUMN_CLIENT_GENDER, measurement.gender);
                        values.put(RecordEntry.COLUMN_HEAD, measurement.head);
                        values.put(RecordEntry.COLUMN_NECK, measurement.neck);
                        values.put(RecordEntry.COLUMN_NECKLINE, measurement.neckline);
                        values.put(RecordEntry.COLUMN_BUST_POINT, measurement.bustpoint);
                        values.put(RecordEntry.COLUMN_UNDER_BUST, measurement.underbust);
                        values.put(RecordEntry.COLUMN_BUST, measurement.bust);
                        values.put(RecordEntry.COLUMN_WAIST, measurement.waist);
                        values.put(RecordEntry.COLUMN_HIP, measurement.hip);
                        values.put(RecordEntry.COLUMN_SHOULDER, measurement.shoulder);
                        values.put(RecordEntry.COLUMN_CHEST, measurement.chest);
                        values.put(RecordEntry.COLUMN_GOWN_LENGTH, measurement.gownlength);
                        values.put(RecordEntry.COLUMN_BLOUSE_LENGTH, measurement.blouselength);
                        values.put(RecordEntry.COLUMN_SHORT_GOWN_LENGTH, measurement.shortGownLength);
                        values.put(RecordEntry.COLUMN_SLEEVE_LENGTH, measurement.sleeveLength);
                        values.put(RecordEntry.COLUMN_ARMHOLE, measurement.armHole);
                        values.put(RecordEntry.COLUMN_KNEE_LENGTH, measurement.kneeLength);
                        values.put(RecordEntry.COLUMN_HALF_LENGTH, measurement.halfLength);
                        values.put(RecordEntry.COLUMN_TROUSER_LENGTH, measurement.trouserLength);
                        values.put(RecordEntry.COLUMN_THIGH, measurement.thigh);
                        values.put(RecordEntry.COLUMN_TROUSER_BOTTOM, measurement.trouserBottom);

                        Uri newUri = getContentResolver().insert(RecordEntry.CONTENT_URI, values);
                        if (newUri == null) {
                            Toast.makeText(MainActivity.this, getString(R.string.insert_fail), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, getString(R.string.insert_successful), Toast.LENGTH_LONG).show();
                        }
                        count++;
                        //Toast.makeText(MainActivity.this, String.valueOf(dataSnapshot.getChildrenCount()) + " " + measurement.name, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Null Reference", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.no_internet_connectivity_toast,
                    (ViewGroup) findViewById(R.id.custom_internet_availability_container));
            TextView text = layout.findViewById(R.id.connection_text);
            text.setText("No internet. Please check your connection and try again");

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
            return false;
        }
    }

    private void openComplaintSuggestion() {
        Intent intent = new Intent(getApplicationContext(), SuggestionComplaints.class);
        startActivity(intent);
    }


}
