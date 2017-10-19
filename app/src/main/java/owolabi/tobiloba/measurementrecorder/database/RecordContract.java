package owolabi.tobiloba.measurementrecorder.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by TOBILOBA on 10/10/2017.
 */

public class RecordContract {

    public static final String CONTENT_AUTHORITY = "owolabi.tobiloba.measurementrecorder";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECORDS = "records";

    private RecordContract(){}

    public static final class RecordEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECORDS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDS;

        public static final String TABLE_NAME = "records";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_CLIENT_TITLE = "title";
        public static final String COLUMN_CLIENT_NAME = "name";
        public static final String COLUMN_CLIENT_GENDER = "gender";
        public static final String COLUMN_HEAD = "head";
        public static final String COLUMN_NECK = "neck";
        public static final String COLUMN_NECKLINE = "neckline";
        public static final String COLUMN_BUST_POINT = "bustpoint";
        public static final String COLUMN_UNDER_BUST = "underbust";
        public static final String COLUMN_BUST = "bust";
        public static final String COLUMN_WAIST = "waist";
        public static final String COLUMN_HIP = "hip";
        public static final String COLUMN_SHOULDER = "shoulder";
        public static final String COLUMN_CHEST = "chest";
        public static final String COLUMN_GOWN_LENGTH = "gownlength";
        public static final String COLUMN_BLOUSE_LENGTH = "blouselength";
        public static final String COLUMN_SHORT_GOWN_LENGTH = "shortgownlength";
        public static final String COLUMN_SLEEVE_LENGTH = "sleeve_length";
        public static final String COLUMN_ARMHOLE = "armhole";
        public static final String COLUMN_KNEE_LENGTH = "kneelength";
        public static final String COLUMN_HALF_LENGTH = "halflength";
        public static final String COLUMN_TROUSER_LENGTH = "trouserlength";
        public static final String COLUMN_THIGH = "thigh";
        public static final String COLUMN_TROUSER_BOTTOM = "trouserbottom";



        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }
}





