package owolabi.tobiloba.measurementrecorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignUpLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);
        setTitle(getString(R.string.signuplogin_activity_title));
    }
}
