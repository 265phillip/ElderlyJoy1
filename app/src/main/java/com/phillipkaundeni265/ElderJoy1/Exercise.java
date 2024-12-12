package com.phillipkaundeni265.ElderJoy1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Exercise extends AppCompatActivity implements SensorEventListener {

    private TextView stepCountTextView;
    private Button startButton, stopButton;
    private boolean isWalking = false;
    private int stepCount = 0;
    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 0;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        stepCountTextView = findViewById(R.id.stepCount);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        startButton.setOnClickListener(v -> startWalking());
        stopButton.setOnClickListener(v -> stopWalking());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
        }
    }

    private void startWalking() {
        isWalking = true;
        stepCount = 0;
        updateStepCount();
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void stopWalking() {
        isWalking = false;
        sensorManager.unregisterListener(this, stepCounterSensor);
    }

    private void updateStepCount() {
        stepCountTextView.setText(String.valueOf(stepCount));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isWalking && event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            updateStepCount();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No action needed.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do the step detection initialization if needed.
            } else {
                // Permission denied, disable step detection functionality.
            }
        }
    }
}
