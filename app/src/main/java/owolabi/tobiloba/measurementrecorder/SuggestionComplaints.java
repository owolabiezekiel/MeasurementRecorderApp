package owolabi.tobiloba.measurementrecorder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SuggestionComplaints extends AppCompatActivity {
//textView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
    private Button gmailButton;
    private Button twitterButton;
    private Button instagramButton;
    private Button whatsappButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_complaints);
        setTitle(R.string.complaint_suggestion_header);

        gmailButton = findViewById(R.id.gmailButton);
        twitterButton = findViewById(R.id.twitterButton);
        instagramButton = findViewById(R.id.instagramButton);
        whatsappButton = findViewById(R.id.whatsappButton);


        gmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail( new String[] {"eowolabi537@stu.ui.edu.ng", "owo.ezekiel@gmail.com"}, getString(R.string.complaint_suggestion_mail_topic));
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Twitter button pressed", Toast.LENGTH_LONG).show();
            }
        });

        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Instagram button pressed", Toast.LENGTH_LONG).show();
            }
        });

        whatsappButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Whatsapp button pressed", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
