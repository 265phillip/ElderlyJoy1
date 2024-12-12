package com.phillipkaundeni265.ElderJoy1;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class Sleep extends AppCompatActivity {

    private TimePicker timePicker;
    private CalendarView calendarView;
    private TextView sleepHoursTextView;
    private int sleepHour, sleepMinute;
    private long selectedDateMillis; // Variable to store selected date
    private Button setButton;
    private static final String CHANNEL_ID = "sleep_notification_channel";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;

    private static final String SLEEP_PREFERENCES = "sleep_preferences"; // SharedPreferences key
    private static final String SLEEP_RECORDS = "sleep_records"; // Key for sleep records

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);

        timePicker = findViewById(R.id.timePicker);
        calendarView = findViewById(R.id.calendarView);
        sleepHoursTextView = findViewById(R.id.sleepHoursTextView);
        setButton = findViewById(R.id.setButton);

        timePicker.setIs24HourView(true);

        // Time change listener
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            sleepHour = hourOfDay;
            sleepMinute = minute;
            calculateSleepHours();
        });

        // Calendar date change listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            selectedDateMillis = selectedDate.getTimeInMillis();
            calculateSleepHours(); // Recalculate hours whenever a date is selected
        });

        // Button click listener
        setButton.setOnClickListener(v -> {
            String sleepDetails = getFormattedDate() + " | " + sleepHoursTextView.getText().toString();
            if (checkNotificationPermission()) {
                sendNotification(sleepDetails);
            } else {
                requestNotificationPermission();
            }

            // Save the sleep details in SharedPreferences
            saveSleepDetails(sleepDetails);
        });

        createNotificationChannel(); // Method to set up notifications
    }

    // Check if notification permission is granted
    private boolean checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // For Android versions below 13, permission is implicitly granted
    }

    // Request notification permission for Android 13+
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
    }

    // Create a notification channel (required for Android 8.0+)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sleep Notification";
            String description = "Channel for sleep notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Send notification with sleep details
    private void sendNotification(String sleepDetails) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications) // Replace with your notification icon
                .setContentTitle("Sleep Record")
                .setContentText(sleepDetails)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build()); // Notification ID is 1
    }

    private void calculateSleepHours() {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);
        int currentMinute = now.get(Calendar.MINUTE);

        Calendar sleepTime = Calendar.getInstance();
        sleepTime.set(Calendar.HOUR_OF_DAY, sleepHour);
        sleepTime.set(Calendar.MINUTE, sleepMinute);

        long difference = now.getTimeInMillis() - sleepTime.getTimeInMillis();
        if (difference < 0) {
            difference += 24 * 60 * 60 * 1000; // add 24 hours if difference is negative
        }

        long hours = (difference / (1000 * 60 * 60)) % 24;
        long minutes = (difference / (1000 * 60)) % 60;

        sleepHoursTextView.setText("Sleep hours: " + hours + " hours and " + minutes + " minutes");
    }

    // Save sleep details to SharedPreferences
    private void saveSleepDetails(String sleepDetails) {
        SharedPreferences sharedPreferences = getSharedPreferences(SLEEP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Retrieve current records
        String existingRecords = sharedPreferences.getString(SLEEP_RECORDS, "");

        // Append new sleep details
        String updatedRecords = existingRecords + sleepDetails + "\n";

        // Save updated records
        editor.putString(SLEEP_RECORDS, updatedRecords);
        editor.apply();
    }

    // Format the selected date
    private String getFormattedDate() {
        if (selectedDateMillis == 0) {
            return "No date selected";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDateMillis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return "Date: " + day + "/" + month + "/" + year;
    }
}
