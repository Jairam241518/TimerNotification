package com.timerapp.timerdelta;

import static android.Manifest.permission.FOREGROUND_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public int p = 100;

    private TextView textView;
    private EditText editText;
    private Button button;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Counter");

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Integer intentTime = intent.getIntExtra("TimeRemaining", 0);
                int intentPercent = intent.getIntExtra("remainingPercent", 100);
                textView.setText(intentTime.toString());
                progressBar.setProgress(intentPercent);

            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void startTimer(View view) {

        Intent intentService = new Intent(this, myService.class);
        Integer timeSet = Integer.parseInt(editText.getText().toString());
        intentService.putExtra("TimerValue", timeSet);
        startService(intentService);
    }
}