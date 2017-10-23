package me.regalstreak.wallpapers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DisclaimerActivity extends AppCompatActivity {

    FloatingActionButton emailFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        findStuff();
        setEmailFab();

    }

    private void findStuff() {
        emailFab = findViewById(R.id.report_email);
    }

    private void setEmailFab() {
        emailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String subject = getResources().getString(R.string.app_name) + ":" + " " + getResources().getString(R.string.email_subject);
                String emailBody = getResources().getString(R.string.email_body_1) + getResources().getString(R.string.enter_links) + getResources().getString(R.string.email_body_2);

                Intent sendMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getResources().getString(R.string.email_id), null));
                sendMail.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendMail.putExtra(Intent.EXTRA_TEXT, emailBody);
                startActivity(Intent.createChooser(sendMail, getResources().getString(R.string.email_client)));

            }
        });
    }

}
