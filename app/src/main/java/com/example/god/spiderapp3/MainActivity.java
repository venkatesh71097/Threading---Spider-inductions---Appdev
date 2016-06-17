package com.example.god.spiderapp3;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Message;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.os.Handler;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    SoundPool sp;
    int idFX1 = -1;
    int idFX2 = -1;
    int idFX3 = -1;
    int nowPlaying = -1;
    float volume = .1f;
    int repeats = 2;
    ArrayAdapter<String> stringArrayAdapter;
    Spinner spinner;

    public int currentimageindex = 0;
    public int count = 0, ctr = 0;
    ImageView slidingimage;

    private int[] IMAGE_IDS = {
            R.drawable.family, R.drawable.ice, R.drawable.roommates
    };
    TextView myTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] song = {"FX1", "FX2", "FX3"};
        stringArrayAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        song);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(stringArrayAdapter);
        // Instantiate our sound pool dependent
// upon which version of Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }

        try {
// Create objects of the 2 required classes
            AssetManager assetManager = this.getAssets();
            AssetFileDescriptor descriptor;
// Load our fx in memory ready for use
            descriptor = assetManager.openFd("FX1.ogg");
            idFX1 = sp.load(descriptor, 0);
            descriptor = assetManager.openFd("FX2.ogg");
            idFX2 = sp.load(descriptor, 0);
            descriptor = assetManager.openFd("FX3.ogg");
            idFX3 = sp.load(descriptor, 0);
        } catch (IOException e) {
// Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
// Now for the spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.
                OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                String temp = String.valueOf(spinner.getSelectedItem());
                if(temp=="FX1") {
                    nowPlaying = sp.play(idFX1, volume, volume, 0, repeats, 1);

                }

                else if(temp=="FX2") {
                    nowPlaying = sp.play(idFX2, volume, volume, 0, repeats, 1);

                }

                else if(temp=="FX3")
                {
                    nowPlaying = sp.play(idFX2, volume, volume, 0, repeats, 1);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }






    public void onStart(View v) {

    Thread t = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (count <= 3)
                                updateTextView();


                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    t.start();

    final Handler mHandler = new Handler();
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            ctr = 0;
            if (count <= 3) {

                count++;
                SlideShow();

            }

        }
    };
    int delay = 1000;

    int period = 3000;

    Timer timer = new Timer();

    timer.scheduleAtFixedRate(new TimerTask() {

        public void run() {

            mHandler.post(mUpdateResults);

        }

    }, delay, period);

}


    private void updateTextView() {

        myTextView=(TextView)findViewById(R.id.myTextView);
        ctr++;
        myTextView.setText(" " + ctr);

    }


    public void onClick(View v) {

        finish();
        Process.killProcess(Process.myPid());
    }

    private void SlideShow() {

        slidingimage = (ImageView) findViewById(R.id.slide);
        slidingimage.setImageResource(IMAGE_IDS[currentimageindex % IMAGE_IDS.length]);

        currentimageindex++;

    }
    public void songstop(View v)
    {
                sp.stop(nowPlaying);

    }
    public void songstart() {

    }
}