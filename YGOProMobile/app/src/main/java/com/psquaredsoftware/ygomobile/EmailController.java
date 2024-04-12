package com.psquaredsoftware.ygomobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.psquaredsoftware.ygomobile.R;

public class EmailController extends AppCompatActivity {

    public Button bug, feedback, general;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.emaillayout);
        bug = findViewById(R.id.bugemail);
        feedback = findViewById(R.id.feedbackemail);
        general = findViewById(R.id.generalemail);

        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"duelingmobilebugs@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Report");
                startActivity(Intent.createChooser(emailIntent, "Send Bug Report"));
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"duelingmobilefeedback@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                startActivity(Intent.createChooser(emailIntent, "Send Feedback"));
            }
        });

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ygomobile@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "General Message");
                startActivity(Intent.createChooser(emailIntent, "Send Message"));
            }
        });

        findViewById(R.id.knownissues).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTutPage = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ygo.ignorelist.com/issues.pdf"));
                startActivity(toTutPage);
            }
        });

    }
}
