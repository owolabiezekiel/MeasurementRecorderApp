package owolabi.tobiloba.measurementrecorder;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import owolabi.tobiloba.measurementrecorder.database.RecordContract.RecordEntry;

/**
 * Created by TOBILOBA on 10/10/2017.
 */

/**
 * Allows user to create a new record or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Identifier for the record data loader
     */
    private static final int EXISTING_RECORD_LOADER = 0;

    /**
     * Content URI for the existing record (null if it's a new pet)
     */
    private Uri mCurrentRecordUri;


    /**
     * EditText field to enter the clients name
     */
    private EditText mNameEditText;
    /**
     * EditText field to enter the clients's head
     */
    private EditText mHeadEditText;
    /**
     * EditText field to enter the clients's neck
     */
    private EditText mNeckEditText;
    /**
     * EditText field to enter the clients neckline
     */
    private EditText mNeckLineEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mBustPointEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mUnderBustEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mBustEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mWaistEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mHipEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mShoulderEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mChestEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mGownLengthEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mBlouseLengthEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mShortGownLengthEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mSleeveLengthEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mArmholeEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mKneeLengthEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mHalfLengthEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mTrouserLengthEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private EditText mThighEditText;
    /**
     * EditText field to enter the clients title
     */
    private EditText mTrouserBottomEditText;
    /**
     * EditText field to enter the clients's gender
     */
    private Spinner mGenderSpinner;
    /**
     * Gender of the client. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    /**
     * Boolean flag that keeps track of whether the pet has been edited (true) or not (false)
     */
    private boolean mRecordHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mRecordHasChanged = true;
            return false;
        }
    };

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Banner Ads
        MobileAds.initialize(this, "ca-app-pub-9965245858402334~6725398448");
        mAdView = findViewById(R.id.editorViewAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9965245858402334/1313334794");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Intent intent = getIntent();
        mCurrentRecordUri = intent.getData();
        if (mCurrentRecordUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_record));
            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(EXISTING_RECORD_LOADER, null, this);
            setTitle(getString(R.string.editor_activity_title_edit_record));
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_client_name);
        mHeadEditText = (EditText) findViewById(R.id.edit_head);
        mNeckEditText = (EditText) findViewById(R.id.edit_neck);
        mNeckLineEditText = (EditText) findViewById(R.id.edit_neckline);
        mBustPointEditText = (EditText) findViewById(R.id.edit_bustpoint);
        mUnderBustEditText = (EditText) findViewById(R.id.edit_underbust);
        mBustEditText = (EditText) findViewById(R.id.edit_bust);
        mWaistEditText = (EditText) findViewById(R.id.edit_waist);
        mHipEditText = (EditText) findViewById(R.id.edit_hip);
        mShoulderEditText = (EditText) findViewById(R.id.edit_shoulder);
        mChestEditText = (EditText) findViewById(R.id.edit_chest);
        mGownLengthEditText = (EditText) findViewById(R.id.edit_gownlength);
        mBlouseLengthEditText = (EditText) findViewById(R.id.edit_blouselength);
        mShortGownLengthEditText = (EditText) findViewById(R.id.edit_shortgownlength);
        mSleeveLengthEditText = (EditText) findViewById(R.id.edit_sleevelength);
        mArmholeEditText = (EditText) findViewById(R.id.edit_armhole);
        mKneeLengthEditText = (EditText) findViewById(R.id.edit_kneelength);
        mHalfLengthEditText = (EditText) findViewById(R.id.edit_halflength);
        mTrouserLengthEditText = (EditText) findViewById(R.id.edit_trouserlength);
        mThighEditText = (EditText) findViewById(R.id.edit_thigh);
        mTrouserBottomEditText = (EditText) findViewById(R.id.edit_trouserbottom);

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mNeckLineEditText.setOnTouchListener(mTouchListener);
        mHeadEditText.setOnTouchListener(mTouchListener);
        mNeckEditText.setOnTouchListener(mTouchListener);
        mBustPointEditText.setOnTouchListener(mTouchListener);
        mUnderBustEditText.setOnTouchListener(mTouchListener);
        mBustEditText.setOnTouchListener(mTouchListener);
        mWaistEditText.setOnTouchListener(mTouchListener);
        mHipEditText.setOnTouchListener(mTouchListener);
        mShoulderEditText.setOnTouchListener(mTouchListener);
        mChestEditText.setOnTouchListener(mTouchListener);
        mGownLengthEditText.setOnTouchListener(mTouchListener);
        mBlouseLengthEditText.setOnTouchListener(mTouchListener);
        mShortGownLengthEditText.setOnTouchListener(mTouchListener);
        mSleeveLengthEditText.setOnTouchListener(mTouchListener);
        mArmholeEditText.setOnTouchListener(mTouchListener);
        mKneeLengthEditText.setOnTouchListener(mTouchListener);
        mHalfLengthEditText.setOnTouchListener(mTouchListener);
        mTrouserLengthEditText.setOnTouchListener(mTouchListener);
        mThighEditText.setOnTouchListener(mTouchListener);
        mTrouserBottomEditText.setOnTouchListener(mTouchListener);


        mGenderSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the client.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = RecordEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = RecordEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = RecordEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }


    private void saveRecord() {
        String clientName = mNameEditText.getText().toString().trim();
        String headString = mHeadEditText.getText().toString().trim();
        String neckString = mNeckEditText.getText().toString().trim();
        String neckLineString = mNeckLineEditText.getText().toString().trim();
        String bustPointString = mBustPointEditText.getText().toString().trim();
        String underBustString = mUnderBustEditText.getText().toString().trim();
        String bustString = mBustEditText.getText().toString().trim();
        String waistString = mWaistEditText.getText().toString().trim();
        String hipString = mHipEditText.getText().toString().trim();
        String shoulderString = mShoulderEditText.getText().toString().trim();
        String chestString = mChestEditText.getText().toString().trim();
        String gownLengthString = mGownLengthEditText.getText().toString().trim();
        String blouseLengthString = mBlouseLengthEditText.getText().toString().trim();
        String shortGownLengthString = mShortGownLengthEditText.getText().toString().trim();
        String sleeveLengthString = mSleeveLengthEditText.getText().toString().trim();
        String armholeString = mArmholeEditText.getText().toString().trim();
        String kneeLengthString = mKneeLengthEditText.getText().toString().trim();
        String halfLengthString = mHalfLengthEditText.getText().toString().trim();
        String trouserLengthString = mTrouserLengthEditText.getText().toString().trim();
        String thighString = mThighEditText.getText().toString().trim();
        String trouserBottomString = mTrouserBottomEditText.getText().toString().trim();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (mCurrentRecordUri == null &&
                TextUtils.isEmpty(clientName)) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.record_not_saved_dialog_msg);
            builder.setPositiveButton(R.string.action_continue_editing, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton(R.string.action_discard_editing, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(EditorActivity.this, MainActivity.class));
                    if (mInterstitialAd.isLoaded()){
                        mInterstitialAd.show();
                    }
                    finish();
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Toast.makeText(this, getString(R.string.some_fields_are_empty), Toast.LENGTH_LONG).show();
            return;
        }



        ContentValues values = new ContentValues();

        values.put(RecordEntry.COLUMN_CLIENT_NAME, clientName);
        values.put(RecordEntry.COLUMN_CLIENT_GENDER, mGender);

        // If some measurements were not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        float head = 0;
        float neck = 0;
        float neckline = 0;
        float bustPoint = 0;
        float underBust = 0;
        float bust = 0;
        float waist = 0;
        float hip = 0;
        float shoulder = 0;
        float chest = 0;
        float gownLength = 0;
        float blouseLength = 0;
        float shortGownLength = 0;
        float sleeveLength = 0;
        float armhole = 0;
        float kneeLength = 0;
        float halfLength = 0;
        float trouserLength = 0;
        float thigh = 0;
        float trouserBottom = 0;

        if (!TextUtils.isEmpty(headString)) {
            head = Float.parseFloat(headString);
        }
        values.put(RecordEntry.COLUMN_HEAD, head);

        if (!TextUtils.isEmpty(neckString)) {
            neck = Float.parseFloat(neckString);
        }
        values.put(RecordEntry.COLUMN_NECK, neck);

        if (!TextUtils.isEmpty(neckLineString)) {
            neckline = Float.parseFloat(neckLineString);
        }
        values.put(RecordEntry.COLUMN_NECKLINE, neckline);


        if (!TextUtils.isEmpty(bustPointString)) {
            bustPoint = Float.parseFloat(bustPointString);
        }
        values.put(RecordEntry.COLUMN_BUST_POINT, bustPoint);


        if (!TextUtils.isEmpty(underBustString)) {
            underBust = Float.parseFloat(underBustString);
        }
        values.put(RecordEntry.COLUMN_UNDER_BUST, underBust);


        if (!TextUtils.isEmpty(bustString)) {
            bust = Float.parseFloat(bustString);
        }
        values.put(RecordEntry.COLUMN_BUST, bust);


        if (!TextUtils.isEmpty(waistString)) {
            waist = Float.parseFloat(waistString);
        }
        values.put(RecordEntry.COLUMN_WAIST, waist);


        if (!TextUtils.isEmpty(hipString)) {
            hip = Float.parseFloat(hipString);
        }
        values.put(RecordEntry.COLUMN_HIP, hip);


        if (!TextUtils.isEmpty(shoulderString)) {
            shoulder = Float.parseFloat(shoulderString);
        }
        values.put(RecordEntry.COLUMN_SHOULDER, shoulder);


        if (!TextUtils.isEmpty(chestString)) {
            chest = Float.parseFloat(chestString);
        }
        values.put(RecordEntry.COLUMN_CHEST, chest);


        if (!TextUtils.isEmpty(gownLengthString)) {
            gownLength = Float.parseFloat(gownLengthString);
        }
        values.put(RecordEntry.COLUMN_GOWN_LENGTH, gownLength);


        if (!TextUtils.isEmpty(blouseLengthString)) {
            blouseLength = Float.parseFloat(blouseLengthString);
        }
        values.put(RecordEntry.COLUMN_BLOUSE_LENGTH, blouseLength);


        if (!TextUtils.isEmpty(shortGownLengthString)) {
            shortGownLength = Float.parseFloat(shortGownLengthString);
        }
        values.put(RecordEntry.COLUMN_SHORT_GOWN_LENGTH, shortGownLength);


        if (!TextUtils.isEmpty(sleeveLengthString)) {
            sleeveLength = Float.parseFloat(sleeveLengthString);
        }
        values.put(RecordEntry.COLUMN_SLEEVE_LENGTH, sleeveLength);

        if (!TextUtils.isEmpty(armholeString)) {
            armhole = Float.parseFloat(armholeString);
        }
        values.put(RecordEntry.COLUMN_ARMHOLE, armhole);


        if (!TextUtils.isEmpty(kneeLengthString)) {
            kneeLength = Float.parseFloat(kneeLengthString);
        }
        values.put(RecordEntry.COLUMN_KNEE_LENGTH, kneeLength);


        if (!TextUtils.isEmpty(halfLengthString)) {
            halfLength = Float.parseFloat(halfLengthString);
        }
        values.put(RecordEntry.COLUMN_HALF_LENGTH, halfLength);


        if (!TextUtils.isEmpty(trouserLengthString)) {
            trouserLength = Float.parseFloat(trouserLengthString);
        }
        values.put(RecordEntry.COLUMN_TROUSER_LENGTH, trouserLength);


        if (!TextUtils.isEmpty(thighString)) {
            thigh = Float.parseFloat(thighString);
        }
        values.put(RecordEntry.COLUMN_THIGH, thigh);


        if (!TextUtils.isEmpty(trouserBottomString)) {
            trouserBottom = Float.parseFloat(trouserBottomString);
        }
        values.put(RecordEntry.COLUMN_TROUSER_BOTTOM, trouserBottom);


        if (mCurrentRecordUri == null) {
            // This is a NEW measurement, so insert a new measurement into the provider,
            // returning the content URI for the new measurement.

            Uri newUri = getContentResolver().insert(RecordEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_fail), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_successful), Toast.LENGTH_LONG).show();
                startActivity(new Intent(EditorActivity.this, MainActivity.class));
            }
        } else {
            // Otherwise this is an EXISTING pet, so update the pet with content URI: mCurrentRecordUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentPetUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentRecordUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_record_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_record_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteRecord();
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
    }  // end showDeleteConfirmationDialog method


    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteRecord() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentRecordUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentRecordUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_record_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_record_successful), Toast.LENGTH_SHORT).show();
            }
        }
        // Close the activity
        finish();
    } // end of deleteRecord method


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentRecordUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            MenuItem shareMenu = menu.findItem(R.id.action_share);
            menuItem.setVisible(false);
            shareMenu.setVisible(false);
        }
        return true;
    }


    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                saveRecord();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the 'Delete' key
            case R.id.action_share:
                createShareIntent();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mRecordHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, navigate to parent activity.
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Code for the share button
     *
     * @return Intent
     */
    private void createShareIntent() {
        String clientName = mNameEditText.getText().toString().trim();
        String headString = mHeadEditText.getText().toString().trim();
        String neckString = mNeckEditText.getText().toString().trim();
        String neckLineString = mNeckLineEditText.getText().toString().trim();
        String bustPointString = mBustPointEditText.getText().toString().trim();
        String underBustString = mUnderBustEditText.getText().toString().trim();
        String bustString = mBustEditText.getText().toString().trim();
        String waistString = mWaistEditText.getText().toString().trim();
        String hipString = mHipEditText.getText().toString().trim();
        String shoulderString = mShoulderEditText.getText().toString().trim();
        String chestString = mChestEditText.getText().toString().trim();
        String gownLengthString = mGownLengthEditText.getText().toString().trim();
        String blouseLengthString = mBlouseLengthEditText.getText().toString().trim();
        String shortGownLengthString = mShortGownLengthEditText.getText().toString().trim();
        String sleeveLengthString = mSleeveLengthEditText.getText().toString().trim();
        String armholeString = mArmholeEditText.getText().toString().trim();
        String kneeLengthString = mKneeLengthEditText.getText().toString().trim();
        String halfLengthString = mHalfLengthEditText.getText().toString().trim();
        String trouserLengthString = mTrouserLengthEditText.getText().toString().trim();
        String thighString = mThighEditText.getText().toString().trim();
        String trouserBottomString = mTrouserBottomEditText.getText().toString().trim();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String recordDetails = "Name:\t" + clientName +
                "\nHead: \t" + headString +
                "\nNeck: \t" + neckString +
                "\nNeckline: \t" + neckLineString +
                "\nBust Point\t" + bustPointString +
                "\nUnder Bust\t" + underBustString +
                "\nBust\t" + bustString +
                "\nWaist\t" + waistString +
                "\nHip\t" + hipString +
                "\nShoulder\t" + shoulderString +
                "\nChest\t" + chestString +
                "\nGown Length:\t" + gownLengthString +
                "\nBlouse Length:\t" + blouseLengthString +
                "\nShort Gown Length:\t" + shortGownLengthString +
                "\nSleeve Length: \t" + sleeveLengthString +
                "\nArmhole: \t" + armholeString +
                "\nKnee Length:\t" + kneeLengthString +
                "\nHalf Length:\t" + halfLengthString +
                "\nTrouser Length:\t" + trouserLengthString +
                "\nThigh:\t" + thighString +
                "\nTrouser Bottom\t" + trouserBottomString;
        shareIntent.putExtra(Intent.EXTRA_TEXT, recordDetails);
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mRecordHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                RecordEntry._ID,
                RecordEntry.COLUMN_CLIENT_NAME,
                RecordEntry.COLUMN_CLIENT_GENDER,
                RecordEntry.COLUMN_HEAD,
                RecordEntry.COLUMN_NECK,
                RecordEntry.COLUMN_NECKLINE,
                RecordEntry.COLUMN_BUST_POINT,
                RecordEntry.COLUMN_UNDER_BUST,
                RecordEntry.COLUMN_BUST,
                RecordEntry.COLUMN_WAIST,
                RecordEntry.COLUMN_HIP,
                RecordEntry.COLUMN_SHOULDER,
                RecordEntry.COLUMN_CHEST,
                RecordEntry.COLUMN_GOWN_LENGTH,
                RecordEntry.COLUMN_BLOUSE_LENGTH,
                RecordEntry.COLUMN_SHORT_GOWN_LENGTH,
                RecordEntry.COLUMN_SLEEVE_LENGTH,
                RecordEntry.COLUMN_ARMHOLE,
                RecordEntry.COLUMN_KNEE_LENGTH,
                RecordEntry.COLUMN_HALF_LENGTH,
                RecordEntry.COLUMN_TROUSER_LENGTH,
                RecordEntry.COLUMN_THIGH,
                RecordEntry.COLUMN_TROUSER_BOTTOM};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentRecordUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_NAME);
            int genderColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_GENDER);
            int headColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_HEAD);
            int neckColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_NECK);
            int neckLineColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_NECKLINE);
            int bustPointColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_BUST_POINT);
            int underBustColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_UNDER_BUST);
            int bustColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_BUST);
            int waistColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_WAIST);
            int hipColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_HIP);
            int shoulderColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_SHOULDER);
            int chestColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_CHEST);
            int gownLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_GOWN_LENGTH);
            int blouseLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_BLOUSE_LENGTH);
            int shortGownLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_SHORT_GOWN_LENGTH);
            int sleeveLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_SLEEVE_LENGTH);
            int armholeColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_ARMHOLE);
            int kneeLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_KNEE_LENGTH);
            int halfLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_HALF_LENGTH);
            int trouserLengthColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_TROUSER_LENGTH);
            int thighColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_THIGH);
            int trouserBottomColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_TROUSER_BOTTOM);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            float head = cursor.getFloat(headColumnIndex);
            float neck = cursor.getFloat(neckColumnIndex);
            float neckline = cursor.getFloat(neckLineColumnIndex);
            float bustPoint = cursor.getFloat(bustPointColumnIndex);
            float underBust = cursor.getFloat(underBustColumnIndex);
            float bust = cursor.getFloat(bustColumnIndex);
            float waist = cursor.getFloat(waistColumnIndex);
            float hip = cursor.getFloat(hipColumnIndex);
            float shoulder = cursor.getFloat(shoulderColumnIndex);
            float chest = cursor.getFloat(chestColumnIndex);
            float gownLength = cursor.getFloat(gownLengthColumnIndex);
            float blouseLength = cursor.getFloat(blouseLengthColumnIndex);
            float shortGownLength = cursor.getFloat(shortGownLengthColumnIndex);
            float sleeveLength = cursor.getFloat(sleeveLengthColumnIndex);
            float armhole = cursor.getFloat(armholeColumnIndex);
            float kneeLength = cursor.getFloat(kneeLengthColumnIndex);
            float halfLength = cursor.getFloat(halfLengthColumnIndex);
            float trouserLength = cursor.getFloat(trouserLengthColumnIndex);
            float thigh = cursor.getFloat(thighColumnIndex);
            float trouserBottom = cursor.getFloat(trouserBottomColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            if (head > 0.0)
                mHeadEditText.setText(Float.toString(head));
            if (neck > 0.0)
                mNeckEditText.setText(Float.toString(neck));
            if (neckline > 0.0)
                mNeckLineEditText.setText(Float.toString(neckline));
            if (bustPoint > 0.0)
                mBustPointEditText.setText(Float.toString(bustPoint));
            if (underBust > 0.0)
                mUnderBustEditText.setText(Float.toString(underBust));
            if (bust > 0.0)
                mBustEditText.setText(Float.toString(bust));
            if (waist > 0.0)
                mWaistEditText.setText(Float.toString(waist));
            if (hip > 0.0)
                mHipEditText.setText(Float.toString(hip));
            if (shoulder > 0.0)
                mShoulderEditText.setText(Float.toString(shoulder));
            if (chest > 0.0)
                mChestEditText.setText(Float.toString(chest));
            if (gownLength > 0.0)
                mGownLengthEditText.setText(Float.toString(gownLength));
            if (blouseLength > 0.0)
                mBlouseLengthEditText.setText(Float.toString(blouseLength));
            if (shortGownLength > 0.0)
                mShortGownLengthEditText.setText(Float.toString(shortGownLength));
            if (sleeveLength > 0.0)
                mSleeveLengthEditText.setText(Float.toString(sleeveLength));
            if (armhole > 0.0)
                mArmholeEditText.setText(Float.toString(armhole));
            if (kneeLength > 0.0)
                mKneeLengthEditText.setText(Float.toString(kneeLength));
            if (halfLength > 0.0)
                mHalfLengthEditText.setText(Float.toString(halfLength));
            if (trouserLength > 0.0)
                mTrouserLengthEditText.setText(Float.toString(trouserLength));
            if (thigh > 0.0)
                mThighEditText.setText(Float.toString(thigh));
            if (trouserBottom > 0.0)
                mTrouserBottomEditText.setText(Float.toString(trouserBottom));


            // Gender is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (gender) {
                case RecordEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case RecordEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mNeckEditText.setText("");
        mHeadEditText.setText("");
        mNeckLineEditText.setText("");
        mBustPointEditText.setText("");
        mUnderBustEditText.setText("");
        mBustEditText.setText("");
        mWaistEditText.setText("");
        mHipEditText.setText("");
        mShoulderEditText.setText("");
        mChestEditText.setText("");
        mGownLengthEditText.setText("");
        mBlouseLengthEditText.setText("");
        mShortGownLengthEditText.setText("");
        mSleeveLengthEditText.setText("");
        mArmholeEditText.setText("");
        mKneeLengthEditText.setText("");
        mHalfLengthEditText.setText("");
        mTrouserLengthEditText.setText("");
        mThighEditText.setText("");
        mTrouserBottomEditText.setText("");


        mGenderSpinner.setSelection(0); // Select "Unknown" gender
    }
}
