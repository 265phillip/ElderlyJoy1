package com.phillipkaundeni265.ElderJoy1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationsActivity extends AppCompatActivity {

    private TextView notificationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);  // Ensure correct layout

        notificationTextView = findViewById(R.id.notificationTextView);  // Correct ID

        // Retrieve the sleep details (date and hours) from the intent
        String sleepDetails = getIntent().getStringExtra("sleepDetails");
        if (sleepDetails != null) {
            notificationTextView.setText(sleepDetails);
        }
    }
}
