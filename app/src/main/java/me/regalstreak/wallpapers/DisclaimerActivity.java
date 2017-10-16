package me.regalstreak.wallpapers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DisclaimerActivity extends AppCompatActivity {

    Button email;
    TextView tptextview;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer);
        findStuff();
        timePass();
    }

    private void findStuff(){
        email = findViewById(R.id.email);
        tptextview = findViewById(R.id.tptextview);
    }
    private void timePass(){;
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                String text = String.valueOf(i);
                tptextview.setText(text);
            }
        });
    }
}
