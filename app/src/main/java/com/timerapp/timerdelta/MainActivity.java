package com.timerapp.timerdelta;

import static android.Manifest.permission.FOREGROUND_SERVICE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int p = 100;

    private TextView textView;
    private EditText editText;
    private Button button;
    private Button pause;
    private Button resume;
    public Boolean isTimerRunning;
    private ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ActivityCompat.requestPermissions(this, new String[]{FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        pause = findViewById(R.id.pauseButton);
        resume = findViewById(R.id.resume);
        resume.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
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
                editText.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                if(intentTime == 0){
                    editText.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    pause.setVisibility(View.INVISIBLE);
                }

            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
    }

    public void startTimer(View view) {

        String editTextString = editText.getText().toString();


        if (editTextString.length() == 0 || Integer.parseInt(editTextString) <= 0 ){
            Toast.makeText(MainActivity.this, "Enter Appropriate Value", Toast.LENGTH_SHORT).show();
            return;
        }else{

            Intent intentService = new Intent(this, myService.class);
            Integer timeSet = Integer.parseInt(editTextString);
            intentService.putExtra("TimerValue", timeSet);
            startService(intentService);

        }


    }

    public void resume(View view) {

        pause.setBackgroundColor(Color.RED);
    }

    public void pause(View view) {

        pause.setBackgroundColor(Color.GREEN);
        resume.setVisibility(View.VISIBLE);


    }
}