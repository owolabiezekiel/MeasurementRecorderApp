package owolabi.tobiloba.measurementrecorder;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import owolabi.tobiloba.measurementrecorder.database.RecordContract.RecordEntry;

/**
 * Created by TOBILOBA on 10/10/2017.
 */

public class RecordCursorAdapter extends CursorAdapter {

    public RecordCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        ImageView displayImage = (ImageView) view.findViewById(R.id.image);


        // Find the columns of pet attributes that we're interested in
        int titleColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_TITLE);
        int nameColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_NAME);
        int genderColumnIndex = cursor.getColumnIndex(RecordEntry.COLUMN_CLIENT_GENDER);


        // Read the pet attributes from the Cursor for the current pet
        String clientTitle = cursor.getString(titleColumnIndex);
        String clientName = cursor.getString(nameColumnIndex);
        int clientGender = cursor.getInt(genderColumnIndex);

        if (TextUtils.isEmpty(clientTitle)) {
            clientTitle = context.getString(R.string.unknown_title);
        }

        //check the client gender and set the approriate image
        // set to a question mark png if the gender is unknown
        if (clientGender == RecordEntry.GENDER_FEMALE){
            displayImage.setImageResource(R.drawable.family_mother);
        } else if (clientGender == RecordEntry.GENDER_UNKNOWN){
            displayImage.setImageResource(R.drawable.color_black);
        } else {
            displayImage.setImageResource(R.drawable.family_father);
        }

        // Update the TextViews with the attributes for the current pet
        titleTextView.setText(clientTitle);
        nameTextView.setText(clientName);
    }
}
