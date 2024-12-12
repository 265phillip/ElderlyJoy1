package com.phillipkaundeni265.ElderJoy1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    private Button selectToneButton;
    private Switch textSpeechToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        selectToneButton = findViewById(R.id.selectToneButton);
        textSpeechToggle = findViewById(R.id.textSpeechToggle);

        // Set up notification tone button listener
        selectToneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open tone selection dialog or action
                // Placeholder action: Show a toast message
                Toast.makeText(SettingsActivity.this, "Select notification tone", Toast.LENGTH_SHORT).show();

                // TODO: Implement actual notification tone selection logic here
            }
        });

        // Set up text-to-speech toggle listener
        textSpeechToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable text-to-speech for login and signup
                Toast.makeText(SettingsActivity.this, "Text-to-Speech Enabled", Toast.LENGTH_SHORT).show();
                // TODO: Implement enabling text-to-speech feature here
            } else {
                // Disable text-to-speech for login and signup
                Toast.makeText(SettingsActivity.this, "Text-to-Speech Disabled", Toast.LENGTH_SHORT).show();
                // TODO: Implement disabling text-to-speech feature here
            }
        });
    }
}
