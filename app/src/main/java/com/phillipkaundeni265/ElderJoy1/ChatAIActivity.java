package com.phillipkaundeni265.ElderJoy1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;

public class ChatAIActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private EditText editText;
    private TextView textView;
    private ExecutorService executorService;
    private static final String BACKEND_URL = "http://your-backend-url/api/generate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_aiactivity);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setSpeechRate(0.8f);
            }
        });

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty()) {
                    String recognizedText = matches.get(0);
                    editText.setText(recognizedText);
                    runLocalModel(recognizedText);
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
                textView.setText("Listening...");
            }

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float rmsdB) {}

            @Override
            public void onBufferReceived(byte[] buffer) {}

            @Override
            public void onEndOfSpeech() {
                textView.setText("Processing...");
            }

            @Override
            public void onError(int error) {
                Toast.makeText(getApplicationContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                textView.setText("Error occurred");
            }

            @Override
            public void onPartialResults(Bundle partialResults) {}

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        executorService = Executors.newSingleThreadExecutor();

        button.setOnClickListener(v -> speechRecognizer.startListening(intent));

        button2.setOnClickListener(v -> {
            String inputText = editText.getText().toString();
            runLocalModel(inputText);
        });
    }

    private void runLocalModel(String inputText) {
        textView.setText("Processing...");
        textToSpeech.speak("Processing", TextToSpeech.QUEUE_FLUSH, null, null);

        executorService.execute(() -> {
            try {
                String output = getModelOutputFromBackend(inputText);
                runOnUiThread(() -> {
                    textView.setText(output);
                    textToSpeech.speak(output, TextToSpeech.QUEUE_FLUSH, null, null);
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> textView.setText("Error occurred"));
            }
        });
    }

    private String getModelOutputFromBackend(String inputText) throws Exception {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8"); // Corrected here
        JSONObject json = new JSONObject();
        json.put("input_text", inputText);

        RequestBody body = RequestBody.create(JSON, json.toString()); // Corrected here
        Request request = new Request.Builder()
                .url(BACKEND_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
