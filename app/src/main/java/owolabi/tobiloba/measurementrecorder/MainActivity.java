package owolabi.tobiloba.measurementrecorder;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import owolabi.tobiloba.measurementrecorder.database.RecordContract.RecordEntry;
import owolabi.tobiloba.measurementrecorder.database.RecordDBHelper;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecordDBHelper mDbHelper;
    private static final int RECORD_LOADER = 0;
    private RecordCursorAdapter mCursorAdapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new RecordDBHelper(this.getBaseContext());

        Toast.makeText(this, getCount()+" records", Toast.LENGTH_LONG).show();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();

                if(mUser == null){
                    Toast.makeText(MainActivity.this, "No user is signed in", Toast.LENGTH_SHORT).show();
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
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(RecordEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from measurement database");
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all_records_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
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


    private void syncRecordToCloud(){
        Toast.makeText(this, "Sorry, This Feauture is still under development at this time", Toast.LENGTH_LONG).show();
    }

    private void signUpOrSignIn(){
        Intent intent = new Intent(MainActivity.this, SignUpLogin.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        disableSomeMenuItems(menu);
        return true;
    }


    private void disableSomeMenuItems(Menu menu){
        MenuItem sync = menu.findItem(R.id.action_sync_record_to_cloud);
        MenuItem signIn = menu.findItem(R.id.action_sign_in);
        if (mUser == null){
            sync.setEnabled(false);
            signIn.setEnabled(true);
        } else {
            sync.setEnabled(true);
            signIn.setEnabled(false);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" and "Sync Database to Cloud" menu option
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;

            case R.id.action_sync_record_to_cloud:
                syncRecordToCloud();
                return true;

            case R.id.action_sign_in:
                signUpOrSignIn();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



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


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
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
