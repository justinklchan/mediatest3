package com.example.mediatest3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioDeviceInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.media.AudioRecord;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {
    static Activity av;
    TextView tv1,tv2;
    EditText editTextNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String[] perms = new String[]{android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this,
                perms,
                1234);
        tv1=(TextView)findViewById(R.id.textView);
        editTextNumber=(EditText)findViewById(R.id.editTextNumber);
        tv2=(TextView)findViewById(R.id.textView2);
        this.av = this;
        enumerateMicrophones();
    }

    public void enumerateMicrophones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Get the AudioManager system service
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            // Get the list of available audio devices
            AudioDeviceInfo[] devices = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS);

            // Loop through each device and filter out the microphones
//            for (AudioDeviceInfo device : devices) {
//                if (device.getType() == AudioDeviceInfo.TYPE_BUILTIN_MIC ||
//                        device.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
//                        device.getType() == AudioDeviceInfo.TYPE_USB_DEVICE ||
//                        device.getType() == AudioDeviceInfo.TYPE_USB_HEADSET ||
//                        device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_SCO ||
//                        device.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP) {

                    // Print or log the information about each microphone
//                    Log.d("MicrophoneEnumerator", "Microphone: " + device.getProductName() +
//                            ", Type: " + device.getType());
//                }
            }
//        } else {
//            Log.d("MicrophoneEnumerator", "AudioDeviceInfo API is not available on this Android version.");
//        }
    }

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private MyRecorder rec;
    public void onstart(View v) {
        long ts = System.currentTimeMillis();
        tv1.setText(ts+"");
        int time = Integer.parseInt(editTextNumber.getText().toString());
        new CountDownTimer(time*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                tv2.setText(""+(millisUntilFinished / 1000));
            }

            public void onFinish() {
                tv2.setText("0:00");
            }
        }.start();
        rec=new MyRecorder(this,ts+"", time);
        rec.start();
//        // Define the sample rate (e.g., 44.1 kHz)
//        int sampleRate = 44100;
//
//        // Define the channel configuration
//        AudioFormat audioFormat = new AudioFormat.Builder()
//                .setSampleRate(sampleRate)
//                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//                .setChannelIndexMask(AudioFormat.CHANNEL_IN_MONO)  // Configuring to record from the front microphone
//                .build();
//
//        // Get the minimum buffer size for the given configuration
//        int bufferSize = AudioRecord.getMinBufferSize(
//                sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,  // Generally use MONO for microphones
//                AudioFormat.ENCODING_PCM_16BIT
//        );
//
//        // Initialize the AudioRecord object
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        audioRecord = new AudioRecord.Builder()
//                .setAudioSource(MediaRecorder.AudioSource.MIC)  // Select microphone as the audio source
//                .setAudioFormat(audioFormat)
//                .setBufferSizeInBytes(bufferSize)
//                .build();
//
//        // Start recording
//        audioRecord.startRecording();
//        isRecording = true;
//
//        long ts = System.currentTimeMillis();
//        tv1.setText(ts+"");
    }

    public void onstop(View v) {
        rec.stop();
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//        }
    }
}